package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.otto.Subscribe;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.ChainSerializable;
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

    private static class FetchPresetsEvent {
        private final List<Preset> presets;
        private final Throwable t;

        FetchPresetsEvent(List<Preset> presets, Throwable t) {
            this.presets = presets;
            this.t = t;
        }
    }

    private static class DeletePresetFailedEvent {
        private final Preset preset;
        private final Throwable t;

        DeletePresetFailedEvent(Preset preset, Throwable t) {
            this.preset = preset;
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
                    .subscribe(presets -> bus.post(new FetchPresetsEvent(presets, null)),
                            throwable -> bus.post(new FetchPresetsEvent(null, throwable)));
        }
    }

    @Override
    public void editPreset(@NonNull Preset preset) {
        pendingPreset.setCandidate(preset);
        ui.showEditPreset();
    }

    @Override
    public void confirmLastAction() {

    }

    @Override
    public void cancelLastAction() {

    }

    @Override
    public void deletePreset(@NonNull Preset preset) {
        logger.log(this, "deletePreset " + preset);
        Observable.fromCallable(() -> presetRepository.remove(preset.getId()))
                .subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .filter(aBoolean -> !aBoolean)
                .subscribe(aBoolean -> bus.post(new DeletePresetFailedEvent(preset, null)),
                        throwable -> bus.post(new DeletePresetFailedEvent(preset, throwable)));
    }

    @Subscribe
    public void onFetchPresetsEvent(FetchPresetsEvent event) {
        logger.log(this, "onFetchPresetsEvent");

        if (!fetchInProgress) {
            logger.log(this, "ignoring event from not retained presenter!");
            return;
        }

        fetchInProgress = false;

        if (ui == null) {
            logger.log(this, "onFetchPresetsEvent when ui == null");
            return;
        }

        if (event.t != null) {
            ui.onFailedToFetchPresets();
        } else {
            ui.onPresetsFetched(pendingPreset.get(), event.presets);
        }
    }

    @Subscribe
    public void onDeletePresetFailedEvent(DeletePresetFailedEvent event) {
        logger.log(this, "onDeletePresetFailedEvent", event.t);

        if (ui == null) {
            logger.log(this, "onDeletePresetFailedEvent when ui == null");
            return;
        }

        ui.onFailedToDeletePreset(event.preset);
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
        ChainSerializable chainSerializable = (ChainSerializable) state;
        super.onRestore(chainSerializable.getState());
        //noinspection ConstantConditions
        fetchInProgress = (boolean) chainSerializable.getNext().getState();
    }

    @Nullable
    @Override
    public Serializable onRetain() {
        //noinspection ConstantConditions
        ChainSerializable chainSerializable = new ChainSerializable(super.onRetain());
        chainSerializable.createNext(fetchInProgress);
        return chainSerializable;
    }
}
