package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

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

    private Confirm lastActionConfirm;
    private int lastActionPresetId;

    private static class FetchPresetsEvent {
        private final List<Preset> presets;
        private final Throwable t;

        FetchPresetsEvent(List<Preset> presets, Throwable t) {
            this.presets = presets;
            this.t = t;
        }
    }

    private static class DeletePresetEvent {
        private final Preset preset;
        private final boolean success;
        private final Throwable t;

        DeletePresetEvent(Preset preset, boolean success, Throwable t) {
            this.preset = preset;
            this.success = success;
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
        logger.log(this, "confirmLastAction " + lastActionConfirm);
        if (lastActionConfirm == Confirm.DELETE_PRESET) {
            int lastActionPresetIdRef = lastActionPresetId;
            Observable.fromCallable(() -> presetRepository.get(lastActionPresetIdRef))
                    .zipWith(Observable.fromCallable(() -> presetRepository.remove(lastActionPresetIdRef)), (preset, aBoolean) -> {
                        ArrayMap<Boolean, Preset> resultMap = new ArrayMap<>();
                        resultMap.put(aBoolean, preset);
                        return resultMap;
                    })
                    .subscribeOn(subscribeOn)
                    .observeOn(observeOn)
                    .subscribe(booleanPresetMap -> {
                        boolean success = booleanPresetMap.keyAt(0);
                        bus.post(new DeletePresetEvent(booleanPresetMap.get(success), success, null));
                    }, throwable -> {
                        bus.post(new DeletePresetEvent(null, false, throwable));
                    });
        } else if (lastActionConfirm == Confirm.GENERATE_FROM_PRESET) {
            int lastActionPresetIdRef = lastActionPresetId;
            Observable.fromCallable(() -> presetRepository.get(lastActionPresetIdRef))
                    .subscribeOn(subscribeOn)
                    .observeOn(observeOn)
                    .subscribe(super::startGeneration,
                            throwable -> logger.log(this, throwable));
        }
        lastActionConfirm = null;
        lastActionPresetId = 0;
    }

    @Override
    public void cancelLastAction() {
        logger.log(this, "cancelLastAction " + lastActionConfirm);
        lastActionConfirm = null;
        lastActionPresetId = 0;
    }

    @Override
    public void deletePreset(@NonNull Preset preset) {
        logger.log(this, "deletePreset " + preset);
        if (pendingPreset.get() == preset) {
            pendingPreset.clear();
            ui.onPresetDeleted(preset);
            return;
        }
        lastActionConfirm = Confirm.DELETE_PRESET;
        lastActionPresetId = preset.getId();
        ui.showConfirmLastAction(lastActionConfirm);
    }

    @Override
    public void startGeneration(@NonNull Preset preset) {
        logger.log(this, "startGeneration " + preset);
        lastActionConfirm = Confirm.GENERATE_FROM_PRESET;
        lastActionPresetId = preset.getId();
        ui.showConfirmLastAction(lastActionConfirm);
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
    public void onDeletePresetEvent(DeletePresetEvent event) {
        logger.log(this, "onDeletePresetEvent");

        if (ui == null) {
            logger.log(this, "onDeletePresetEvent when ui == null");
            return;
        }

        if (event.t != null || !event.success || event.preset == null) {
            ui.onFailedToDeletePreset();
        } else {
            ui.onPresetDeleted(event.preset);
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

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onRestore(@NonNull Serializable state) {
        ChainSerializable chainSerializable = (ChainSerializable) state;
        super.onRestore(chainSerializable.getState());
        chainSerializable = chainSerializable.getNext();
        fetchInProgress = (boolean) chainSerializable.getState();
        chainSerializable = chainSerializable.getNext();
        lastActionConfirm = (Confirm) chainSerializable.getState();
        chainSerializable = chainSerializable.getNext();
        lastActionPresetId = (int) chainSerializable.getState();
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public Serializable onRetain() {
        ChainSerializable chainSerializable = new ChainSerializable(super.onRetain());
        chainSerializable.createNext(fetchInProgress).createNext(lastActionConfirm).createNext(lastActionPresetId);
        return chainSerializable;
    }
}
