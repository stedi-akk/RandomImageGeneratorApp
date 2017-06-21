package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI;
import com.stedi.randomimagegenerator.generators.ColoredNoiseGenerator;

public interface ColoredNoiseParamsPresenter extends Presenter<ColoredNoiseParamsPresenter.UIImpl> {
    void getValues();

    void setOrientation(@NonNull ColoredNoiseGenerator.Orientation orientation);

    void setType(@NonNull ColoredNoiseGenerator.Type type);

    interface UIImpl extends UI {
        void showOrientation(@NonNull ColoredNoiseGenerator.Orientation orientation);

        void showType(@NonNull ColoredNoiseGenerator.Type type);
    }
}
