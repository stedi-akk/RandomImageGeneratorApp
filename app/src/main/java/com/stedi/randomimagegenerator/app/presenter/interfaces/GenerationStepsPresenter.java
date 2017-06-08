package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.Preset;

public interface GenerationStepsPresenter extends GenerationPresenter<GenerationStepsPresenter.UIImpl> {
    void setIsNew(boolean isNew);

    void release();

    @NonNull
    Preset getCandidate();

    interface UIImpl extends GenerationPresenter.UIImpl {
        void showFirstStep();

        void showLastStep();
    }
}
