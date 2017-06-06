package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.otto.Subscribe;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.EffectGeneratorParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.GeneratorParams;
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ApplyGenerationPresenter;

import java.io.Serializable;

import rx.Observable;
import rx.Scheduler;

public class ApplyGenerationPresenterImpl implements ApplyGenerationPresenter {
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

        public OnPresetSaveEvent(boolean success, Throwable throwable) {
            this.success = success;
            this.throwable = throwable;
        }
    }

    public ApplyGenerationPresenterImpl(PendingPreset pendingPreset,
                                        PresetRepository presetRepository,
                                        Scheduler subscribeOn, Scheduler observeOn,
                                        CachedBus bus, Logger logger) {
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
    public void savePreset() {
        if (saveInProgress)
            return;
        saveInProgress = true;

        Observable.fromCallable(() -> presetRepository.save(pendingPreset.getCandidate()))
                .subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .subscribe(aBoolean ->
                                bus.post(new OnPresetSaveEvent(true, null))
                        , throwable ->
                                bus.post(new OnPresetSaveEvent(false, throwable)));
    }

    @Override
    public void onAttach(@NonNull UIImpl ui) {
        this.ui = ui;
        bus.register(this);
        showPresetDetails();
    }

    @Subscribe
    public void onPresetSaveEvent(OnPresetSaveEvent event) {
        saveInProgress = false;
        if (event.throwable == null && event.success) {
            if (pendingPreset.getCandidate() == pendingPreset.get())
                pendingPreset.clear();
            else
                pendingPreset.setCandidate(null);
        }

        if (ui == null) {
            logger.log(this, "onPresetSaveEvent when ui == null");
            return;
        }

        if (event.throwable != null || !event.success) {
            ui.failedToSavePreset();
            return;
        }

        ui.finishGeneration();
    }

    private void showPresetDetails() {
        Preset preset = pendingPreset.getCandidate();

        GeneratorParams generatorParams = preset.getGeneratorParams();
        if (generatorParams.getType().isEffect()) {
            GeneratorType generatorType = ((EffectGeneratorParams) generatorParams).getTarget().getType();
            ui.showGeneratorType(generatorType);
            ui.showEffectType(generatorParams.getType());
        } else {
            ui.showGeneratorType(generatorParams.getType());
        }

        ui.showQuality(preset.getQuality());
        ui.showSize(preset.getWidth(), preset.getHeight());
        ui.showCount(preset.getCount());
    }

    @Override
    public void onDetach() {
        bus.unregister(this);
        this.ui = null;
    }

    @Override
    public void onRestore(@NonNull Serializable state) {
        saveInProgress = (boolean) state;
    }

    @Nullable
    @Override
    public Serializable onRetain() {
        return saveInProgress;
    }
}
