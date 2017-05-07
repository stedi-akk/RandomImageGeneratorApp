package com.stedi.randomimagegenerator.app.presenter;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.presenter.core.RetainedPresenter;
import com.stedi.randomimagegenerator.app.presenter.core.UI;

public interface ChooseGeneratorPresenter extends RetainedPresenter<ChooseGeneratorPresenter.UIImpl> {
    enum Type {
        FLAT_COLOR,
        COLORED_PIXELS,
        COLORED_CIRCLES,
        COLORED_RECTANGLE,
        COLORED_NOISE
    }

    void getGeneratorTypes();

    void chooseGeneratorType(@NonNull Type type);

    interface UIImpl extends UI {
        void showTypesToChoose(@NonNull Type[] types);

        void onGeneratorTypeChose(@NonNull Type type);

        void showEditGeneratorParams(@NonNull Type type);
    }
}
