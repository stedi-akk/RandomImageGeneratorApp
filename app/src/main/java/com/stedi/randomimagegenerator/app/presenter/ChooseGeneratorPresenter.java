package com.stedi.randomimagegenerator.app.presenter;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.GeneratorParams;
import com.stedi.randomimagegenerator.app.presenter.core.RetainedPresenter;
import com.stedi.randomimagegenerator.app.presenter.core.UI;

public interface ChooseGeneratorPresenter extends RetainedPresenter<ChooseGeneratorPresenter.UIImpl> {
    void getGeneratorTypes();

    void chooseGeneratorType(@NonNull GeneratorType type);

    void editChoseGeneratorParams();

    interface UIImpl extends UI {
        void showTypesToChoose(@NonNull GeneratorType[] types);

        void showEditGeneratorParams(@NonNull GeneratorParams params);
    }
}
