package com.stedi.randomimagegenerator.app.presenter.interfaces;

import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI;

public interface GenerationStepsPresenter extends Presenter<GenerationStepsPresenter.UIImpl> {
    void setIsNew(boolean isNew);

    void release();

    interface UIImpl extends UI {
        void showFirstStep();

        void showFinishStep();
    }
}
