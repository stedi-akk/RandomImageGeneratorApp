package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI;

public interface ChooseSizeAndCountPresenter extends Presenter<ChooseSizeAndCountPresenter.UIImpl> {
    public enum Error {
        INCORRECT_WIDTH,
        INCORRECT_HEIGHT,
        INCORRECT_WIDTH_RANGE,
        INCORRECT_HEIGHT_RANGE,
        INCORRECT_COUNT
    }

    void getValues();

    void setWidth(int width);

    void setHeight(int height);

    void setWidthRange(int from, int to, int step);

    void setHeightRange(int from, int to, int step);

    void setCount(int count);

    interface UIImpl extends UI {
        void showWidth(int width);

        void showHeight(int height);

        void showWidthRange(int from, int to, int step);

        void showHeightRange(int from, int to, int step);

        void showCount(int count);

        void onError(@NonNull Error error);
    }
}
