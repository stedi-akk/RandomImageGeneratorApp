package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.otto.Subscribe;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.HomePresenter;

import java.io.Serializable;
import java.util.List;

import rx.Observable;
import rx.Scheduler;

public class HomePresenterImpl extends HomePresenter {
    private final PresetRepository presetRepository;
    private final PendingPreset pendingPreset;
    private final Scheduler subscribeOn;
    private final Scheduler observeOn;
    private final CachedBus bus;
    private final Logger logger;

    private UIImpl ui;
    private boolean fetchInProgress;

    private static class Event {
        private final List<Preset> presets;
        private final Throwable t;

        Event(List<Preset> presets, Throwable t) {
            this.presets = presets;
            this.t = t;
        }
    }

    public HomePresenterImpl(@NonNull PresetRepository presetRepository, @NonNull PendingPreset pendingPreset,
                             @NonNull Scheduler subscribeOn, @NonNull Scheduler observeOn,
                             @NonNull CachedBus bus, @NonNull Logger logger) {
        super(subscribeOn, observeOn, bus, logger);
        this.presetRepository = presetRepository;
        this.pendingPreset = pendingPreset;
        this.subscribeOn = subscribeOn;
        this.observeOn = observeOn;
        this.bus = bus;
        this.logger = logger;
    }

    @Override
    public void fetchPresets() {
        if (!fetchInProgress) {
            logger.log(this, "fetching presets");
            fetchInProgress = true;
            Observable.fromCallable(presetRepository::getAll)
                    .subscribeOn(subscribeOn)
                    .observeOn(observeOn)
                    .subscribe(presets -> bus.post(new Event(presets, null)),
                            throwable -> bus.post(new Event(null, throwable)));
        }
    }

    @Override
    public void editPreset(@NonNull Preset preset) {
        pendingPreset.setCandidate(preset);
        ui.showEditPreset();
    }

    @Override
    public void startGeneration(@NonNull Preset preset) {

    }

    @Override
    public void confirmLastAction() {

    }

    @Override
    public void cancelLastAction() {

    }

    @Override
    public void openFolder(@NonNull Preset preset) {

    }

    @Override
    public void deletePreset(@NonNull Preset preset) {

    }

    @Subscribe
    public void onEvent(Event event) {
        logger.log(this, "onEvent");

        if (!fetchInProgress) {
            logger.log(this, "ignoring event from not retained presenter!");
            return;
        }

        fetchInProgress = false;

        if (ui == null) {
            logger.log(this, "onEvent when ui == null");
            return;
        }

        if (event.t != null) {
            ui.onFailedToFetchPresets();
        } else {
            ui.onPresetsFetched(pendingPreset.get(), event.presets);
        }
    }

    @Override
    public void onAttach(@NonNull UIImpl ui) {
        super.onAttach(ui);
        this.ui = ui;
        bus.register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bus.unregister(this);
        ui = null;
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
