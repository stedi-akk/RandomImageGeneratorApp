package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationStepsPresenter;

public class GenerationStepsPresenterImpl implements GenerationStepsPresenter {
    private final PendingPreset pendingPreset;
    private final Logger logger;

    private UIImpl ui;

    public GenerationStepsPresenterImpl(@NonNull PendingPreset pendingPreset, @NonNull Logger logger) {
        this.pendingPreset = pendingPreset;
        this.logger = logger;
    }

    @Override
    public void setIsNew(boolean isNew) {
        if (isNew) {
            pendingPreset.newDefaultCandidate();
            ui.showFirstStep();
        } else {
            if (pendingPreset.getCandidate() == null)
                throw new IllegalStateException("pending preset candidate must not be null");
            ui.showFinishStep();
        }
    }

    @Override
    public void release() {
        logger.log(this, "release called");
        pendingPreset.killCandidate();
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
