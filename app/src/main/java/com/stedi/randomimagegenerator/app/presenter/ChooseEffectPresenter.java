package com.stedi.randomimagegenerator.app.presenter;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.presenter.core.RetainedPresenter;
import com.stedi.randomimagegenerator.app.presenter.core.UI;

public interface ChooseEffectPresenter extends RetainedPresenter<ChooseEffectPresenter.UIImpl> {
    void getEffectTypes();

    void chooseEffectType(@NonNull GeneratorType effectType);

    interface UIImpl extends UI {
        void showTypesToChoose(@NonNull GeneratorType[] types);
    }
}
