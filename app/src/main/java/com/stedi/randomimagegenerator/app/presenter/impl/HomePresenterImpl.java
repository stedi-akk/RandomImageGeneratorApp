package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.otto.Subscribe;
import com.stedi.randomimagegenerator.app.BuildConfig;
import com.stedi.randomimagegenerator.app.di.qualifiers.DefaultScheduler;
import com.stedi.randomimagegenerator.app.di.qualifiers.RigScheduler;
import com.stedi.randomimagegenerator.app.di.qualifiers.UiScheduler;
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
        private final Throwable throwable;

        FetchPresetsEvent(List<Preset> presets, Throwable throwable) {
            this.presets = presets;
            this.throwable = throwable;
        }
    }

    private static class DeletePresetEvent {
        private final Preset preset;
        private final Throwable throwable;

        DeletePresetEvent(Preset preset, Throwable throwable) {
            this.preset = preset;
            this.throwable = throwable;
        }
    }

    public HomePresenterImpl(@NonNull PresetRepository presetRepository, @NonNull PendingPreset pendingPreset,
                             @NonNull @DefaultScheduler Scheduler subscribeOn,
                             @NonNull @RigScheduler Scheduler superSubscribeOn,
                             @NonNull @UiScheduler Scheduler observeOn,
                             @NonNull CachedBus bus, @NonNull Logger logger) {
        super(superSubscribeOn, observeOn, bus, logger);
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
        pendingPreset.prepareCandidateFrom(preset);
        ui.showEditPreset();
    }

    @Override
    public void confirmLastAction() {
        logger.log(this, "confirmLastAction " + lastActionConfirm);
        if (lastActionConfirm == Confirm.DELETE_PRESET) {
            int lastActionPresetIdRef = lastActionPresetId;
            Observable.fromCallable(() -> {
                Preset preset = presetRepository.get(lastActionPresetIdRef);
                presetRepository.remove(lastActionPresetIdRef);
                return preset;
            }).subscribeOn(subscribeOn)
                    .observeOn(observeOn)
                    .subscribe(preset -> bus.post(new DeletePresetEvent(preset, null)),
                            throwable -> bus.post(new DeletePresetEvent(null, throwable)));
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
        if (lastActionConfirm != null) {
            logger.log(this, "ignoring deletePreset, because last action " + lastActionConfirm + " is not confirmed/canceled");
            return;
        }
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
        if (lastActionConfirm != null) {
            logger.log(this, "ignoring startGeneration, because last action " + lastActionConfirm + " is not confirmed/canceled");
            return;
        }
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

        if (ui == null) {
            fetchInProgress = false;
            logger.log(this, "onFetchPresetsEvent when ui == null");
            return;
        }

        if (event.throwable != null) {
            logger.log(this, event.throwable);
            ui.onFailedToFetchPresets();
        } else {
            if (!BuildConfig.BUILD_TYPE.equals("release")) {
                logger.log(this, "onPresetsFetched " + pendingPreset);
                for (Preset preset : event.presets) {
                    logger.log(this, "onPresetsFetched " + preset);
                }
            }
            ui.onPresetsFetched(pendingPreset.get(), event.presets);
        }

        fetchInProgress = false;
    }

    @Subscribe
    public void onDeletePresetEvent(DeletePresetEvent event) {
        logger.log(this, "onDeletePresetEvent");

        if (ui == null) {
            logger.log(this, "onDeletePresetEvent when ui == null");
            return;
        }

        if (event.throwable != null || event.preset == null) {
            logger.log(this, "failed to delete preset", event.throwable);
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
