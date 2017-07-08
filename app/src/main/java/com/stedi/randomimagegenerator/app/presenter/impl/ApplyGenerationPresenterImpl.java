package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.otto.Subscribe;
import com.stedi.randomimagegenerator.app.di.qualifiers.DefaultScheduler;
import com.stedi.randomimagegenerator.app.di.qualifiers.RigScheduler;
import com.stedi.randomimagegenerator.app.di.qualifiers.UiScheduler;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.ChainSerializable;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ApplyGenerationPresenter;

import java.io.Serializable;

import rx.Observable;
import rx.Scheduler;

public class ApplyGenerationPresenterImpl extends ApplyGenerationPresenter {
    private final PendingPreset pendingPreset;
    private final PresetRepository presetRepository;
    private final Scheduler subscribeOn;
    private final Scheduler observeOn;
    private final CachedBus bus;
    private final Logger logger;

    private UIImpl ui;

    private boolean saveInProgress;

    private static class OnPresetSaveEvent {
        private final boolean success;
        private final Throwable throwable;

        OnPresetSaveEvent(boolean success, Throwable throwable) {
            this.success = success;
            this.throwable = throwable;
        }
    }

    public ApplyGenerationPresenterImpl(@NonNull PendingPreset pendingPreset,
                                        @NonNull PresetRepository presetRepository,
                                        @NonNull @RigScheduler Scheduler superSubscribeOn,
                                        @NonNull @DefaultScheduler Scheduler subscribeOn,
                                        @NonNull @UiScheduler Scheduler observeOn,
                                        @NonNull CachedBus bus, @NonNull Logger logger) {
        super(superSubscribeOn, observeOn, bus, logger);
        if (pendingPreset.getCandidate() == null)
            throw new IllegalStateException("pending preset candidate must not be null");
        this.pendingPreset = pendingPreset;
        this.presetRepository = presetRepository;
        this.logger = logger;
        this.subscribeOn = subscribeOn;
        this.observeOn = observeOn;
        this.bus = bus;
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
        this.ui = null;
    }

    @NonNull
    @Override
    public Preset getPreset() {
        return pendingPreset.getCandidate();
    }

    @Override
    public boolean isPresetNewOrChanged() {
        return pendingPreset.isCandidateNewOrChanged();
    }

    @Override
    public void savePreset(@NonNull String name) {
        if (saveInProgress)
            return;
        saveInProgress = true;

        Preset preset = pendingPreset.getCandidate();
        preset.setName(name);
        Observable.fromCallable(() -> presetRepository.save(preset))
                .subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .subscribe(aBoolean -> bus.post(new OnPresetSaveEvent(aBoolean, null)),
                        throwable -> bus.post(new OnPresetSaveEvent(false, throwable)));
    }

    @Subscribe
    public void onPresetSaveEvent(OnPresetSaveEvent event) {
        logger.log(this, "onPresetSaveEvent");
        saveInProgress = false;

        if (event.throwable == null && event.success) {
            if (pendingPreset.getCandidate() == pendingPreset.get())
                pendingPreset.clear();
            pendingPreset.candidateSaved();
        }

        if (ui == null) {
            logger.log(this, "onPresetSaveEvent when ui == null");
            return;
        }

        if (event.throwable != null || !event.success) {
            ui.failedToSavePreset();
            return;
        }

        ui.onPresetSaved();
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void startGeneration(@NonNull Preset preset) {
        if (preset != pendingPreset.getCandidate())
            throw new IllegalArgumentException("candidate preset is required");
        if (pendingPreset.isCandidateNewOrChanged())
            pendingPreset.applyCandidate();
        super.startGeneration(pendingPreset.getCandidate());
    }

    @Override
    public void onRestore(@NonNull Serializable state) {
        ChainSerializable chainSerializable = (ChainSerializable) state;
        super.onRestore(chainSerializable.getState());
        chainSerializable = chainSerializable.getNext();
        //noinspection ConstantConditions
        saveInProgress = (boolean) chainSerializable.getState();
    }

    @Nullable
    @Override
    public Serializable onRetain() {
        //noinspection ConstantConditions
        ChainSerializable chainSerializable = new ChainSerializable(super.onRetain());
        chainSerializable.createNext(saveInProgress);
        return chainSerializable;
    }
}
