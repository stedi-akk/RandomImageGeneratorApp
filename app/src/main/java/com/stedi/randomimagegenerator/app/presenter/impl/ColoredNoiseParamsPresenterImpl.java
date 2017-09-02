package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.ColoredNoiseParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ColoredNoiseParamsPresenter;
import com.stedi.randomimagegenerator.generators.ColoredNoiseGenerator;

public class ColoredNoiseParamsPresenterImpl implements ColoredNoiseParamsPresenter {
    private final PendingPreset pendingPreset;

    private UIImpl ui;
    private ColoredNoiseParams params;

    public ColoredNoiseParamsPresenterImpl(@NonNull PendingPreset pendingPreset) {
        this.pendingPreset = pendingPreset;
    }

    @Override
    public void getValues() {
        ui.showOrientation(params.getNoiseOrientation());
        ui.showType(params.getNoiseType());
    }

    @Override
    public void setOrientation(@NonNull ColoredNoiseGenerator.Orientation orientation) {
        params.setNoiseOrientation(orientation);
    }

    @Override
    public void setType(@NonNull ColoredNoiseGenerator.Type type) {
        params.setNoiseType(type);
    }

    @Override
    public void onAttach(@NonNull UIImpl ui) {
        this.ui = ui;
        GeneratorParams currentParams = pendingPreset.getCandidate().getGeneratorParams();
        if (currentParams instanceof EffectGeneratorParams) {
            params = (ColoredNoiseParams) ((EffectGeneratorParams) currentParams).getTarget();
        } else {
            params = (ColoredNoiseParams) currentParams;
        }
    }

    @Override
    public void onDetach() {
        this.ui = null;
    }
}
