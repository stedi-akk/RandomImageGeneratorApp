package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.squareup.otto.Subscribe;
import com.stedi.randomimagegenerator.ImageParams;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.RetainedPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI;

import rx.Scheduler;

abstract class GenerationPresenter<T extends GenerationPresenter.UIImpl> implements RetainedPresenter<T> {
    private final Scheduler subscribeOn;
    private final Scheduler observeOn;
    private final CachedBus bus;
    private final Logger logger;

    private UIImpl ui;
    private boolean generationInProgress;

    private static class Event {

    }

    protected GenerationPresenter(@NonNull Scheduler subscribeOn, @NonNull Scheduler observeOn, @NonNull CachedBus bus, @NonNull Logger logger) {
        this.subscribeOn = subscribeOn;
        this.observeOn = observeOn;
        this.bus = bus;
        this.logger = logger;
    }

    public void startGeneration(@NonNull Preset preset) {

    }

    private Object busTarget = new Object() {
        @Subscribe
        public void onEvent(Event event) {

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

    interface UIImpl extends UI {
        void onStartGeneration();

        void onGenerated(@NonNull ImageParams imageParams);

        void onGenerationUnknownError();

        void onFailedToGenerate(@NonNull ImageParams imageParams);

        void onFinishGeneration();
    }
}
