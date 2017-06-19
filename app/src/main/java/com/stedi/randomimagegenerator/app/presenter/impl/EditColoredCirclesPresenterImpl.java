package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.ColoredCirclesParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.EditColoredCirclesPresenter;

public class EditColoredCirclesPresenterImpl implements EditColoredCirclesPresenter {
    private final PendingPreset pendingPreset;
    private final Logger logger;

    private UIImpl ui;
    private ColoredCirclesParams params;

    public EditColoredCirclesPresenterImpl(@NonNull PendingPreset pendingPreset, @NonNull Logger logger) {
        if (pendingPreset.getCandidate() == null)
            throw new IllegalStateException("pending preset candidate must not be null");
        this.pendingPreset = pendingPreset;
        this.logger = logger;
    }

    @Override
    public void getValues() {
        if (params.getCount() == null) {
            ui.showRandomCount();
        } else {
            ui.showCount(params.getCount());
        }
    }

    @Override
    public boolean setRandomCount() {
        logger.log(this, "setRandomCount");
        params.setRandomCount();
        return true;
    }

    @Override
    public boolean setCount(int count) {
        if (count < 1) {
            ui.showErrorIncorrectCount();
            return false;
        }
        logger.log(this, "setCount " + count);
        params.setCount(count);
        return true;
    }

    @Override
    public void onAttach(@NonNull UIImpl ui) {
        logger.log(this, "onAttach");
        this.ui = ui;
        GeneratorParams currentParams = pendingPreset.getCandidate().getGeneratorParams();
        if (currentParams instanceof EffectGeneratorParams) {
            params = (ColoredCirclesParams) ((EffectGeneratorParams) currentParams).getTarget();
        } else {
            params = (ColoredCirclesParams) currentParams;
        }
    }

    @Override
    public void onDetach() {
        logger.log(this, "onDetach");
        this.ui = null;
    }
}
