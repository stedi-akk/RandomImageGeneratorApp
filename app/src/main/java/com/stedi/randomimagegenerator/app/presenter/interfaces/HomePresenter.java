package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.RetainedPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI;

import java.util.List;

public interface HomePresenter extends RetainedPresenter<HomePresenter.UIImpl> {
    void fetchPresets();

    void createNewGeneration();

    interface UIImpl extends UI {
        void onPresetsFetched(@NonNull PendingPreset pendingPreset, @NonNull List<Preset> presets);

        void onFailedToFetch(@NonNull Throwable t);

        void onShowNewGeneration();
    }
}
