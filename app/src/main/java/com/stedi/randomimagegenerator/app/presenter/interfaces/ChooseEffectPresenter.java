package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI;

public interface ChooseEffectPresenter extends Presenter<ChooseEffectPresenter.UIImpl> {
    void getEffectTypes();

    void chooseEffectType(@Nullable GeneratorType effectType);

    interface UIImpl extends UI {
        void showTypes(@NonNull GeneratorType[] types, @Nullable GeneratorType selectedType);
    }
}
