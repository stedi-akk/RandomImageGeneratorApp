package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseEffectPresenter;

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
        GeneratorParams currentParams = pendingPreset.getCandidate().getGeneratorParams();
        GeneratorType effectType;
        GeneratorType targetType;
        if (currentParams instanceof EffectGeneratorParams) {
            effectType = currentParams.getType();
            targetType = ((EffectGeneratorParams) currentParams).getTarget().getType();
        } else {
            effectType = null;
            targetType = currentParams.getType();
        }
        ui.showTypes(GeneratorType.effectTypes(), effectType, targetType);
    }

    @Override
    public void chooseEffectType(@Nullable GeneratorType effectType) {
        GeneratorParams newParams;
        GeneratorParams prevParams = pendingPreset.getCandidate().getGeneratorParams();
        if (prevParams instanceof EffectGeneratorParams) {
            prevParams = ((EffectGeneratorParams) prevParams).getTarget();
        }
        if (effectType != null) {
            newParams = GeneratorParams.Companion.createDefaultEffectParams(effectType, prevParams);
        } else {
            newParams = prevParams;
        }
        pendingPreset.getCandidate().setGeneratorParams(newParams);
        logger.log(this, "chooseGeneratorType result = " + pendingPreset.getCandidate().getGeneratorParams());
    }

    @Override
    public void onAttach(@NonNull UIImpl ui) {
        this.ui = ui;
    }

    @Override
    public void onDetach() {
        this.ui = null;
    }
}
