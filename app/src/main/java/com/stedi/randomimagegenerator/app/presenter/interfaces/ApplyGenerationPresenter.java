package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.RetainedPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI;

public interface ApplyGenerationPresenter extends RetainedPresenter<ApplyGenerationPresenter.UIImpl> {
    Preset getPreset();

    void savePreset(@NonNull String name);

    interface UIImpl extends UI {
        void onPresetSaved();

        void failedToSavePreset();
    }
}
