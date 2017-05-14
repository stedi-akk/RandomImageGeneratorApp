package com.stedi.randomimagegenerator.app.presenter.interfaces;

import com.stedi.randomimagegenerator.app.presenter.interfaces.core.RetainedPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI;

public interface ChooseSizeAndCountPresenter extends RetainedPresenter<ChooseSizeAndCountPresenter.UIImpl> {
    void setCount(int count);

    void setWidth(int width);

    void setHeight(int height);

    interface UIImpl extends UI {
        void showCount(int count);

        void showSize(int width, int height);

        void showErrorIncorrectCount();

        void showErrorIncorrectSize();
    }
}
