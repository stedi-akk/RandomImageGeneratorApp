package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.RetainedPresenter;

public interface ChooseGeneratorPresenter extends RetainedPresenter<ChooseGeneratorPresenter.UIImpl> {
    void getGeneratorTypes();

    void chooseGeneratorType(@NonNull GeneratorType type);

    void editChoseGeneratorParams();

    interface UIImpl extends RetainedPresenter.RetainedUI {
        void showTypesToChoose(@NonNull GeneratorType[] types);

        void showEditGeneratorParams(@NonNull GeneratorType type);
    }
}
