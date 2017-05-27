package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.EffectGeneratorParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ApplyGenerationPresenter;

import java.io.Serializable;

public class ApplyGenerationPresenterImpl implements ApplyGenerationPresenter {
    private final PendingPreset pendingPreset;
    private final Logger logger;

    private UIImpl ui;

    public ApplyGenerationPresenterImpl(PendingPreset pendingPreset, Logger logger) {
        this.pendingPreset = pendingPreset;
        this.logger = logger;
    }

    @Override
    public void startGeneration() {
        ui.finishGeneration();
    }

    @Override
    public void savePreset() {
        ui.finishGeneration();
    }

    @Override
    public void onAttach(@NonNull UIImpl ui) {
        this.ui = ui;
        Preset preset = pendingPreset.get();

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
        this.ui = null;
    }

    @Override
    public void onRestore(@NonNull Serializable state) {

    }

    @Nullable
    @Override
    public Serializable onRetain() {
        return null;
    }
}
