package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationPresenter;

public class GenerationPresenterImpl implements GenerationPresenter {
    private final PendingPreset pendingPreset;
    private final Logger logger;

    private UIImpl ui;

    public GenerationPresenterImpl(PendingPreset pendingPreset, Logger logger) {
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
    public void cancel() {
        pendingPreset.setCandidate(null);
    }

    @Override
    public void generate() {
        if (pendingPreset.getCandidate().getId() == 0)
            pendingPreset.applyCandidate();
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
