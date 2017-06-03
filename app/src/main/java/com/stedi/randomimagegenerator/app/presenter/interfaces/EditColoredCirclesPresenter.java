package com.stedi.randomimagegenerator.app.presenter.interfaces;

import com.stedi.randomimagegenerator.app.presenter.interfaces.core.RetainedPresenter;

public interface EditColoredCirclesPresenter extends RetainedPresenter<EditColoredCirclesPresenter.UIImpl> {
    void setRandomCount();

    void setCount(int count);

    void confirm();

    void cancel();

    interface UIImpl extends RetainedPresenter.RetainedUI {
        void showRandomCount();

        void showCount(int count);

        void showErrorIncorrectCount();

        void finishView();
    }
}
