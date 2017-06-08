package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationStepsPresenter;

import java.io.Serializable;

public class GenerationStepsPresenterImpl implements GenerationStepsPresenter {
    private final PendingPreset pendingPreset;
    private final Logger logger;

    private UIImpl ui;

    public GenerationStepsPresenterImpl(PendingPreset pendingPreset, Logger logger) {
        this.pendingPreset = pendingPreset;
        this.logger = logger;
    }

    @Override
    public void setIsNew(boolean isNew) {
        if (isNew) {
            pendingPreset.newCandidate();
            ui.showFirstStep();
        } else {
            ui.showLastStep();
        }
    }

    @Override
    public void release() {
        logger.log(this, "release called");
        pendingPreset.setCandidate(null);
    }

    @NonNull
    @Override
    public Preset getCandidate() {
        return pendingPreset.getCandidate();
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

    @Override
    public void startGeneration(@NonNull Preset preset) {
        if (pendingPreset.getCandidate().getId() == 0)
            pendingPreset.applyCandidate();
    }
}
