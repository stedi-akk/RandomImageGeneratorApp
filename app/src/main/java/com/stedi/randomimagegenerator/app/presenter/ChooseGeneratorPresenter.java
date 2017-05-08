package com.stedi.randomimagegenerator.app.presenter;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.presenter.core.RetainedPresenter;
import com.stedi.randomimagegenerator.app.presenter.core.UI;

public interface ChooseGeneratorPresenter extends RetainedPresenter<ChooseGeneratorPresenter.UIImpl> {
    void getGeneratorTypes();

    void chooseGeneratorType(@NonNull GeneratorType type);

    interface UIImpl extends UI {
        void showTypesToChoose(@NonNull GeneratorType[] types);

        void onGeneratorTypeChose(@NonNull GeneratorType type);

        void showEditGeneratorParams(@NonNull GeneratorType type);
    }
}
