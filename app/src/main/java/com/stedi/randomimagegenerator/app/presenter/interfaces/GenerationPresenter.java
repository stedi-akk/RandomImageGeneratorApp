package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.graphics.Bitmap;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.otto.Subscribe;
import com.stedi.randomimagegenerator.ImageParams;
import com.stedi.randomimagegenerator.Rig;
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
        private final Throwable throwable;

        public Event(Type type, ImageParams imageParams, Throwable throwable) {
            this.type = type;
            this.imageParams = imageParams;
            this.throwable = throwable;
        }
    }

    GenerationPresenter(@NonNull Scheduler subscribeOn, @NonNull Scheduler observeOn, @NonNull CachedBus bus, @NonNull Logger logger) {
        this.subscribeOn = subscribeOn;
        this.observeOn = observeOn;
        this.bus = bus;
        this.logger = logger;
    }

    public void startGeneration(@NonNull Preset preset) {
        if (generationInProgress)
            return;

        logger.log(this, "GENERATION STARTED");
        generationInProgress = true;
        Completable.fromAction(() -> {
            new Rig.Builder()
                    .setGenerator(preset.getGeneratorParams().createGenerator())
                    .setCount(preset.getCount())
                    .setFixedSize(preset.getWidth(), preset.getHeight())
                    .setQuality(preset.getQuality())
                    .setFileSavePath(preset.getSaveFolder())
                    .setCallback(new GenerateCallback() {
                        @Override
                        public void onGenerated(ImageParams imageParams, Bitmap bitmap) {

                        }

                        @Override
                        public void onFailedToGenerate(ImageParams imageParams, Exception e) {

                        }
                    })
                    .setFileSaveCallback(new SaveCallback() {
                        @Override
                        public void onSaved(Bitmap bitmap, File file) {

                        }

                        @Override
                        public void onFailedToSave(Bitmap bitmap, Exception e) {

                        }
                    })
                    .build().generate();
        }).subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .subscribe(() -> {
                }, throwable -> {
                    bus.post(new Event(Event.Type.ON_GENERATION_UNKNOWN_ERROR, null, throwable));
                });
    }

    private Object busTarget = new Object() {
        @Subscribe
        public void onEvent(Event event) {
            if (ui == null) {
                generationInProgress = false;
                logger.log(this, "busTarget onEvent ui == null");
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
