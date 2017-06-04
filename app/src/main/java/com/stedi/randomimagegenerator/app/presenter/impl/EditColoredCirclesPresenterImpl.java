package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.ColoredCirclesParams;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.EditColoredCirclesPresenter;

import java.io.Serializable;

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
    public void setRandomCount() {
        params.setRandomCount();
        ui.showRandomCount();
    }

    @Override
    public void setCount(int count) {
    }

    @Override
    public void confirm() {

    }

    @Override
    public void cancel() {

    }

    @Override
    public void onAttach(@NonNull UIImpl ui) {
        this.ui = ui;
        this.params = (ColoredCirclesParams) pendingPreset.getCandidate().getGeneratorParams();
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
