package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.di.qualifiers.RigScheduler;
import com.stedi.randomimagegenerator.app.di.qualifiers.UiScheduler;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationStepsPresenter;

import rx.Scheduler;

public class GenerationStepsPresenterImpl extends GenerationStepsPresenter {
    private final PendingPreset pendingPreset;
    private final Logger logger;

    private UIImpl ui;

    public GenerationStepsPresenterImpl(@NonNull PendingPreset pendingPreset,
                                        @NonNull @RigScheduler Scheduler subscribeOn,
                                        @NonNull @UiScheduler Scheduler observeOn,
                                        @NonNull CachedBus bus, @NonNull Logger logger) {
        super(subscribeOn, observeOn, bus, logger);
        this.pendingPreset = pendingPreset;
        this.logger = logger;
    }

    @Override
    public void setIsNew(boolean isNew) {
        if (isNew) {
            pendingPreset.newCandidate();
            ui.showFirstStep();
        } else {
            ui.showFinishStep();
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
        super.onAttach(ui);
        this.ui = ui;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.ui = null;
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void startGeneration(@NonNull Preset preset) {
        if (pendingPreset.getCandidate().getId() == 0)
            pendingPreset.applyCandidate();
        super.startGeneration(preset);
    }
}
