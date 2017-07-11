package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.Manifest;
import android.graphics.Bitmap;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;

import com.squareup.otto.Subscribe;
import com.stedi.randomimagegenerator.ImageParams;
import com.stedi.randomimagegenerator.Rig;
import com.stedi.randomimagegenerator.app.di.qualifiers.RigScheduler;
import com.stedi.randomimagegenerator.app.di.qualifiers.UiScheduler;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.RetainedPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI;
import com.stedi.randomimagegenerator.callbacks.GenerateCallback;
import com.stedi.randomimagegenerator.callbacks.SaveCallback;

import java.io.File;
import java.io.Serializable;

import rx.Completable;
import rx.Scheduler;
import rx.functions.Action0;

abstract class GenerationPresenter<T extends GenerationPresenter.UIImpl> implements RetainedPresenter<T> {
    private final Scheduler subscribeOn;
    private final Scheduler observeOn;
    private final CachedBus bus;
    private final Logger logger;

    private UIImpl ui;
    private boolean generationInProgress;

    private static class Event {
        private enum Type {
            ON_START_GENERATION,
            ON_GENERATION_UNKNOWN_ERROR,
            ON_FINISH_GENERATION,
            ON_GENERATED,
            ON_FAILED_TO_GENERATE
        }

        private final Type type;
        private final ImageParams imageParams;

        Event(Type type, ImageParams imageParams) {
            this.type = type;
            this.imageParams = imageParams;
        }
    }

    GenerationPresenter(@NonNull @RigScheduler Scheduler subscribeOn,
                        @NonNull @UiScheduler Scheduler observeOn,
                        @NonNull CachedBus bus, @NonNull Logger logger) {
        this.subscribeOn = subscribeOn;
        this.observeOn = observeOn;
        this.bus = bus;
        this.logger = logger;
    }

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void startGeneration(@NonNull Preset preset) {
        if (generationInProgress)
            return;
        generationInProgress = true;
        logger.log(this, "GENERATION STARTED");
        bus.post(new Event(Event.Type.ON_START_GENERATION, null));

        Completable.fromAction(new Action0() {
            private ImageParams generationFor;

            @Override
            public void call() {
                Rig.Builder builder = new Rig.Builder();
                builder.setGenerator(preset.getGeneratorParams().getGenerator());

                if (preset.getWidth() != 0)
                    builder.setFixedWidth(preset.getWidth());
                if (preset.getHeight() != 0)
                    builder.setFixedHeight(preset.getHeight());
                if (preset.getCount() != 0)
                    builder.setCount(preset.getCount());

                if (preset.getWidthRange() != null) {
                    int[] a = preset.getWidthRange();
                    builder.setWidthRange(a[0], a[1], a[2]);
                }

                if (preset.getHeightRange() != null) {
                    int[] a = preset.getHeightRange();
                    builder.setHeightRange(a[0], a[1], a[2]);
                }

                builder.setQuality(preset.getQuality());
                builder.setFileSavePath(preset.getSaveFolder());
                builder.setCallback(new GenerateCallback() {
                    @Override
                    public void onGenerated(ImageParams imageParams, Bitmap bitmap) {
                        generationFor = imageParams;
                    }

                    @Override
                    public void onFailedToGenerate(ImageParams imageParams, Exception e) {
                        logger.log(this, e);
                        generationFor = imageParams;
                    }
                });
                builder.setFileSaveCallback(new SaveCallback() {
                    @Override
                    public void onSaved(Bitmap bitmap, File file) {
                        final ImageParams generationForRef = generationFor;
                        Completable.fromAction(() -> bus.post(new Event(Event.Type.ON_GENERATED, generationForRef))).subscribeOn(observeOn).subscribe();
                    }

                    @Override
                    public void onFailedToSave(Bitmap bitmap, Exception e) {
                        logger.log(this, e);
                        final ImageParams generationForRef = generationFor;
                        Completable.fromAction(() -> bus.post(new Event(Event.Type.ON_FAILED_TO_GENERATE, generationForRef))).subscribeOn(observeOn).subscribe();
                    }
                });
                builder.build().generate();
            }
        }).subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .subscribe(() -> {
                    logger.log(this, "GENERATION FINISHED");
                    bus.post(new Event(Event.Type.ON_FINISH_GENERATION, null));
                }, throwable -> {
                    logger.log(this, throwable);
                    bus.post(new Event(Event.Type.ON_GENERATION_UNKNOWN_ERROR, null));
                });
    }

    private Object busTarget = new Object() {
        @Subscribe
        public void onEvent(Event event) {
            if (ui == null) {
                generationInProgress = false;
                logger.log(GenerationPresenter.this, "busTarget onEvent ui == null");
                return;
            }

            switch (event.type) {
                case ON_START_GENERATION:
                    ui.onStartGeneration();
                    break;
                case ON_GENERATION_UNKNOWN_ERROR:
                    generationInProgress = false;
                    ui.onGenerationUnknownError();
                    break;
                case ON_FINISH_GENERATION:
                    generationInProgress = false;
                    ui.onFinishGeneration();
                    break;
                case ON_GENERATED:
                    ui.onGenerated(event.imageParams);
                    break;
                case ON_FAILED_TO_GENERATE:
                    ui.onFailedToGenerate(event.imageParams);
                    break;
                default:
                    throw new IllegalStateException("unreachable code");
            }
        }
    };

    @CallSuper
    @Override
    public void onAttach(@NonNull T ui) {
        this.ui = ui;
        bus.register(busTarget);
    }

    @CallSuper
    @Override
    public void onDetach() {
        bus.unregister(busTarget);
        ui = null;
    }

    @CallSuper
    @Nullable
    @Override
    public Serializable onRetain() {
        return generationInProgress;
    }

    @CallSuper
    @Override
    public void onRestore(@NonNull Serializable state) {
        generationInProgress = (boolean) state;
    }

    interface UIImpl extends UI {
        void onStartGeneration();

        void onGenerated(@NonNull ImageParams imageParams);

        void onGenerationUnknownError();

        void onFailedToGenerate(@NonNull ImageParams imageParams);

        void onFinishGeneration();
    }
}
