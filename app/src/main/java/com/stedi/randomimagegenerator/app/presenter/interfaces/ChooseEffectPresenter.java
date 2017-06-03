package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.RetainedPresenter;

public interface ChooseEffectPresenter extends RetainedPresenter<ChooseEffectPresenter.UIImpl> {
    void getEffectTypes();

    void chooseEffectType(@NonNull GeneratorType effectType);

    interface UIImpl extends RetainedPresenter.RetainedUI {
        void showTypesToChoose(@NonNull GeneratorType[] types);
    }
}
