package com.stedi.randomimagegenerator.app.presenter.interfaces;

import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI;

public interface SimpleIntegerParamsPresenter extends Presenter<SimpleIntegerParamsPresenter.UIImpl> {
    void getValues();

    boolean canBeRandom();

    boolean setRandomValue();

    boolean setValue(int value);

    interface UIImpl extends UI {
        void showRandomValue();

        void showValue(int value);

        void showErrorIncorrectValue();
    }
}
