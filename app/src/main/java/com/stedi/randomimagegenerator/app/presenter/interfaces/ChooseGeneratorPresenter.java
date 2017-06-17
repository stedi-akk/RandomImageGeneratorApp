package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.RetainedPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI;

public interface ChooseGeneratorPresenter extends RetainedPresenter<ChooseGeneratorPresenter.UIImpl> {
    void getGeneratorTypes();

    void chooseGeneratorType(@NonNull GeneratorType type);

    void editChoseGeneratorParams();

    interface UIImpl extends UI {
        void showTypes(@NonNull GeneratorType[] types, @NonNull GeneratorType selectedType);

        void showEditGeneratorParams(@NonNull GeneratorType type);
    }
}
