package com.stedi.randomimagegenerator.app.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository;
import com.stedi.randomimagegenerator.app.other.logger.Logger;

import java.io.Serializable;
import java.util.List;

import rx.Observable;
import rx.Scheduler;

public class HomePresenterImpl implements HomePresenter {
    private final PresetRepository presetRepository;
    private final Scheduler subscribeOn;
    private final Scheduler observeOn;
    private final Bus bus;
    private final Logger logger;

    private UIImpl ui;
    private boolean fetchInProgress;

    private static class Event {
        private final List<Preset> presets;
        private final Throwable t;

        public Event(List<Preset> presets, Throwable t) {
            this.presets = presets;
            this.t = t;
        }
    }

    public HomePresenterImpl(PresetRepository presetRepository, Scheduler subscribeOn, Scheduler observeOn, Bus bus, Logger logger) {
        this.presetRepository = presetRepository;
        this.subscribeOn = subscribeOn;
        this.observeOn = observeOn;
        this.bus = bus;
        this.logger = logger;
    }

    @Override
    public void fetchPresets() {
        if (!fetchInProgress) {
            fetchInProgress = true;
            Observable.fromCallable(presetRepository::getAll)
                    .subscribeOn(subscribeOn)
                    .observeOn(observeOn)
                    .subscribe(presets -> bus.post(new Event(presets, null)),
                            throwable -> bus.post(new Event(null, throwable)));
        }
    }

    @Subscribe
    public void onEvent(Event event) {
        fetchInProgress = false;

        if (ui == null) {
            logger.log(this, "onEvent when ui == null");
            return;
        }

        if (event.t != null)
            ui.onFailedToFetch(event.t);
        else
            ui.onPresetsFetched(event.presets);
    }

    @Override
    public void onAttach(@NonNull UIImpl ui) {
        this.ui = ui;
        bus.register(this);
    }

    @Override
    public void onDetach() {
        bus.unregister(this);
        this.ui = null;
    }

    @Override
    public void onRestore(@NonNull Serializable state) {
        fetchInProgress = (boolean) state;
    }

    @Nullable
    @Override
    public Serializable onRetain() {
        return fetchInProgress;
    }
}
