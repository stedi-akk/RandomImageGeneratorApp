package com.stedi.randomimagegenerator.app.presenter.interfaces;

import com.stedi.randomimagegenerator.app.presenter.interfaces.core.RetainedPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI;

public interface EditColoredCirclesPresenter extends RetainedPresenter<EditColoredCirclesPresenter.UIImpl> {
    void setRandomCount();

    void setCount(int count);

    void confirm();

    void cancel();

    interface UIImpl extends UI {
        void showRandomCount();

        void showCount(int count);

        void showErrorIncorrectCount();

        void finishView();
    }
}
