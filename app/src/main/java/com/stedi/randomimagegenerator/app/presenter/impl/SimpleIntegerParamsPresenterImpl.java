package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.SimpleIntegerParams;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.SimpleIntegerParamsPresenter;

public class SimpleIntegerParamsPresenterImpl implements SimpleIntegerParamsPresenter {
    private final PendingPreset pendingPreset;
    private final Logger logger;

    private UIImpl ui;
    private SimpleIntegerParams params;

    public SimpleIntegerParamsPresenterImpl(@NonNull PendingPreset pendingPreset, @NonNull Logger logger) {
        if (pendingPreset.getCandidate() == null)
            throw new IllegalStateException("pending preset candidate must not be null");
        this.pendingPreset = pendingPreset;
        this.logger = logger;
    }

    @Override
    public void getValues() {
        if (params.getValue() == null) {
            ui.showRandomValue();
        } else {
            ui.showValue(params.getValue());
        }
    }

    @Override
    public boolean canBeRandom() {
        return params.canBeRandom();
    }

    @Override
    public void setRandomValue() {
        if (!params.canBeRandom())
            throw new IllegalStateException("setRandomValue called when canBeRandom is false");
        logger.log(this, "setRandomValue");
        params.setRandomValue();
    }

    @Override
    public boolean setValue(int value) {
        if (value < 1) {
            ui.showErrorIncorrectValue();
            return false;
        }
        logger.log(this, "setValue " + value);
        params.setValue(value);
        return true;
    }

    @Override
    public void onAttach(@NonNull UIImpl ui) {
        logger.log(this, "onAttach");
        this.ui = ui;
        GeneratorParams currentParams = pendingPreset.getCandidate().getGeneratorParams();
        if (currentParams instanceof EffectGeneratorParams) {
            params = (SimpleIntegerParams) ((EffectGeneratorParams) currentParams).getTarget();
        } else {
            params = (SimpleIntegerParams) currentParams;
        }
    }

    @Override
    public void onDetach() {
        logger.log(this, "onDetach");
        this.ui = null;
    }
}
