package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.RetainedPresenter;

public interface ApplyGenerationPresenter extends RetainedPresenter<ApplyGenerationPresenter.UIImpl> {
    void startGeneration();

    void savePreset();

    interface UIImpl extends RetainedPresenter.RetainedUI {
        void showGeneratorType(@NonNull GeneratorType type);

        void showEffectType(@NonNull GeneratorType effectType);

        void showQuality(@NonNull Quality quality);

        void showSize(int width, int height);

        void showCount(int count);

        void finishGeneration();

        void failedToSavePreset();
    }
}
