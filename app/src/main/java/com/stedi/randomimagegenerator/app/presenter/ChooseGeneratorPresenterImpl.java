package com.stedi.randomimagegenerator.app.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.logger.Logger;

import java.io.Serializable;

public class ChooseGeneratorPresenterImpl implements ChooseGeneratorPresenter {
    private final PendingPreset pendingPreset;
    private final Logger logger;

    private UIImpl ui;

    public ChooseGeneratorPresenterImpl(PendingPreset pendingPreset, Logger logger) {
        this.pendingPreset = pendingPreset;
        this.logger = logger;
    }

    @Override
    public void getGeneratorTypes() {
        ui.showTypesToChoose(GeneratorType.values());
    }

    @Override
    public void chooseGeneratorType(@NonNull GeneratorType type) {
        pendingPreset.get().setGeneratorParams(GeneratorParams.createDefaultFromType(type));
    }

    @Override
    public void editChoseGeneratorParams() {
        ui.showEditGeneratorParams(pendingPreset.get().getGeneratorParams());
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
