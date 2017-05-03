package com.stedi.randomimagegenerator.app.presenter;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.presenter.core.RetainedPresenter;
import com.stedi.randomimagegenerator.app.presenter.core.UI;

import java.util.List;

public interface HomePresenter extends RetainedPresenter<HomePresenter.UIImpl> {
    void fetchPresets();

    void createNewGeneration();

    interface UIImpl extends UI {
        void onPresetsFetched(@NonNull List<Preset> presets);

        void onFailedToFetch(@NonNull Throwable t);

        void onShowNewGeneration();
    }
}
