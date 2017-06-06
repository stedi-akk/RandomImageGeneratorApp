package com.stedi.randomimagegenerator.app.presenter.interfaces;

import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI;

public interface GenerationPresenter extends Presenter<GenerationPresenter.UIImpl> {
    void setIsNew(boolean isNew);

    void cancel();

    void generate();

    interface UIImpl extends UI {
        void showFirstStep();

        void showLastStep();
    }
}
