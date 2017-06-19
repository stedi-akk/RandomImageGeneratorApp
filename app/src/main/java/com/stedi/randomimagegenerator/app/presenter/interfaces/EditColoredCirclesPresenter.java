package com.stedi.randomimagegenerator.app.presenter.interfaces;

import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI;

public interface EditColoredCirclesPresenter extends Presenter<EditColoredCirclesPresenter.UIImpl> {
    void getValues();

    boolean setRandomCount();

    boolean setCount(int count);

    interface UIImpl extends UI {
        void showRandomCount();

        void showCount(int count);

        void showErrorIncorrectCount();
    }
}
