package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.otto.Subscribe;
import com.stedi.randomimagegenerator.app.di.DefaultScheduler;
import com.stedi.randomimagegenerator.app.di.RigScheduler;
import com.stedi.randomimagegenerator.app.di.RootSavePath;
import com.stedi.randomimagegenerator.app.di.UiScheduler;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.ChainSerializable;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ApplyGenerationPresenter;

import java.io.File;
import java.io.Serializable;

import rx.Completable;
import rx.Scheduler;

public class ApplyGenerationPresenterImpl extends ApplyGenerationPresenter {
    private final PendingPreset pendingPreset;
    private final PresetRepository presetRepository;
    private final String rootSavePath;
    private final Scheduler subscribeOn;
    private final Scheduler observeOn;
    private final CachedBus bus;
    private final Logger logger;

    private UIImpl ui;

    private boolean saveInProgress;

    private static class OnPresetSaveEvent {
        private final Throwable throwable;

        OnPresetSaveEvent(Throwable throwable) {
            this.throwable = throwable;
        }
    }

    public ApplyGenerationPresenterImpl(@NonNull PendingPreset pendingPreset,
                                        @NonNull PresetRepository presetRepository,
                                        @NonNull @RootSavePath String rootSavePath,
                                        @NonNull @RigScheduler Scheduler superSubscribeOn,
                                        @NonNull @DefaultScheduler Scheduler subscribeOn,
                                        @NonNull @UiScheduler Scheduler observeOn,
                                        @NonNull CachedBus bus, @NonNull Logger logger) {
        super(superSubscribeOn, observeOn, bus, logger);
        if (pendingPreset.getCandidate() == null)
            throw new IllegalStateException("pending preset candidate must not be null");
        this.pendingPreset = pendingPreset;
        this.presetRepository = presetRepository;
        this.rootSavePath = rootSavePath;
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
        String originalName = preset.getName();
        long originalTimestamp = preset.getTimestamp();
        String originalSavePath = preset.getPathToSave();

        Completable.fromCallable(() -> {
            preset.setName(name);
            preset.setTimestamp(System.currentTimeMillis());
            presetRepository.save(preset);
            preset.setPathToSave(rootSavePath + File.separator + preset.getId());
            presetRepository.save(preset);
            return true;
        }).subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .subscribe(() -> bus.post(new OnPresetSaveEvent(null)),
                        throwable -> {
                            preset.setName(originalName);
                            preset.setTimestamp(originalTimestamp);
                            preset.setPathToSave(originalSavePath);
                            bus.post(new OnPresetSaveEvent(throwable));
                        });
    }

    @Subscribe
    public void onPresetSaveEvent(OnPresetSaveEvent event) {
        logger.log(this, "onPresetSaveEvent");
        saveInProgress = false;

        if (event.throwable == null) {
            if (pendingPreset.getCandidate() == pendingPreset.get())
                pendingPreset.clear();
            pendingPreset.candidateSaved();
        }

        if (ui == null) {
            logger.log(this, "onPresetSaveEvent when ui == null");
            return;
        }

        if (event.throwable != null) {
            logger.log(this, event.throwable);
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
        super.onRestore(chainSerializable.get());
        chainSerializable = chainSerializable.getChain();
        //noinspection ConstantConditions
        saveInProgress = (boolean) chainSerializable.get();
    }

    @Nullable
    @Override
    public Serializable onRetain() {
        //noinspection ConstantConditions
        ChainSerializable chainSerializable = new ChainSerializable(super.onRetain());
        chainSerializable.addChain(saveInProgress);
        return chainSerializable;
    }
}
