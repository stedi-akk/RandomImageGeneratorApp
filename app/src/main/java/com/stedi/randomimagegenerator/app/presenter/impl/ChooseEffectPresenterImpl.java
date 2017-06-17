package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseEffectPresenter;

import java.io.Serializable;

public class ChooseEffectPresenterImpl implements ChooseEffectPresenter {
    private final PendingPreset pendingPreset;
    private final Logger logger;

    private UIImpl ui;

    public ChooseEffectPresenterImpl(@NonNull PendingPreset pendingPreset, @NonNull Logger logger) {
        if (pendingPreset.getCandidate() == null)
            throw new IllegalStateException("pending preset candidate must not be null");
        this.pendingPreset = pendingPreset;
        this.logger = logger;
    }

    @Override
    public void getEffectTypes() {
        ui.showTypes(new GeneratorType[]{
                GeneratorType.MIRRORED,
                GeneratorType.TEXT_OVERLAY
        }, pendingPreset.getCandidate().getGeneratorParams().getType());
    }

    @Override
    public void chooseEffectType(@NonNull GeneratorType effectType) {
        Preset preset = pendingPreset.getCandidate();
        GeneratorParams effectParams = GeneratorParams.createDefaultEffectParams(effectType, preset.getGeneratorParams());
        preset.setGeneratorParams(effectParams);
    }

    @Override
    public void onAttach(@NonNull UIImpl ui) {
        this.ui = ui;
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
