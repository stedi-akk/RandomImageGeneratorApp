package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.logger.Logger;

import rx.Scheduler;

public abstract class GenerationStepsPresenter extends GenerationPresenter<GenerationStepsPresenter.UIImpl> {
    public GenerationStepsPresenter(@NonNull Scheduler subscribeOn, @NonNull Scheduler observeOn, @NonNull CachedBus bus, @NonNull Logger logger) {
        super(subscribeOn, observeOn, bus, logger);
    }

    public abstract void setIsNew(boolean isNew);

    public abstract void release();

    @NonNull
    public abstract Preset getCandidate();

    public interface UIImpl extends GenerationPresenter.UIImpl {
        void showFirstStep();

        void showLastStep();
    }
}
