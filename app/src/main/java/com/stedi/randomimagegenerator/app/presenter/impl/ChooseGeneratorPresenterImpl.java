package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseGeneratorPresenter;

import java.io.Serializable;

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
        ui.showTypesToChoose(new GeneratorType[]{
                GeneratorType.FLAT_COLOR,
                GeneratorType.COLORED_PIXELS,
                GeneratorType.COLORED_CIRCLES,
                GeneratorType.COLORED_RECTANGLE,
                GeneratorType.COLORED_NOISE,
        });
    }

    @Override
    public void chooseGeneratorType(@NonNull GeneratorType type) {
        pendingPreset.getCandidate().setGeneratorParams(GeneratorParams.createDefaultParams(type));
    }

    @Override
    public void editChoseGeneratorParams() {
        ui.showEditGeneratorParams(pendingPreset.getCandidate().getGeneratorParams().getType());
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
