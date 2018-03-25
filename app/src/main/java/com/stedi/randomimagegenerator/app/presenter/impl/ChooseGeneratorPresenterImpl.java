package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseGeneratorPresenter;

public class ChooseGeneratorPresenterImpl implements ChooseGeneratorPresenter {
    private final PendingPreset pendingPreset;
    private final Logger logger;

    private UIImpl ui;

    public ChooseGeneratorPresenterImpl(@NonNull PendingPreset pendingPreset, @NonNull Logger logger) {
        if (pendingPreset.getCandidate() == null)
            throw new IllegalStateException("pending preset candidate must not be null");
        this.pendingPreset = pendingPreset;
        this.logger = logger;
    }

    @Override
    public void getGeneratorTypes() {
        GeneratorType selectedType;
        GeneratorParams selectedParams = pendingPreset.getCandidate().getGeneratorParams();
        if (selectedParams instanceof EffectGeneratorParams) {
            selectedType = ((EffectGeneratorParams) selectedParams).getTarget().getType();
        } else {
            selectedType = selectedParams.getType();
        }
        ui.showTypes(GeneratorType.nonEffectTypes(), selectedType);
    }

    @Override
    public void chooseGeneratorType(@NonNull GeneratorType type) {
        GeneratorParams newParams = GeneratorParams.Companion.createDefaultParams(type);
        GeneratorParams currentParams = pendingPreset.getCandidate().getGeneratorParams();
        if (currentParams instanceof EffectGeneratorParams) {
            ((EffectGeneratorParams) currentParams).setTargetParams(newParams);
        } else {
            pendingPreset.getCandidate().setGeneratorParams(newParams);
        }
        logger.log(this, "chooseGeneratorType result = " + pendingPreset.getCandidate().getGeneratorParams());
    }

    @Override
    public void editChoseGeneratorParams() {
        GeneratorParams currentParams = pendingPreset.getCandidate().getGeneratorParams();
        if (currentParams instanceof EffectGeneratorParams) {
            ui.showEditGeneratorParams(((EffectGeneratorParams) currentParams).getTarget().getType());
        } else {
            ui.showEditGeneratorParams(currentParams.getType());
        }
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
