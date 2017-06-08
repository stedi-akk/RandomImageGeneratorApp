package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.ImageParams;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.RetainedPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI;

public interface GenerationPresenter<T extends GenerationPresenter.UIImpl> extends RetainedPresenter<T> {
    void startGeneration(@NonNull Preset preset);

    interface UIImpl extends UI {
        void onStartGeneration();

        void onGenerated(@NonNull ImageParams imageParams);

        void onFailedToGenerate(@NonNull ImageParams imageParams);

        void onFinishGeneration();
    }
}
