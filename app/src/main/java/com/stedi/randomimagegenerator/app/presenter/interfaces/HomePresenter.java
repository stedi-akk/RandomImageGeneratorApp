package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.RetainedPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI;

import java.util.List;

public interface HomePresenter extends RetainedPresenter<HomePresenter.UIImpl> {
    void fetchPresets();

    void editPreset(@NonNull Preset preset);

    void generateFromPreset(@NonNull Preset preset);

    void confirmLastAction();

    void cancelLastAction();

    void openFolder(@NonNull Preset preset);

    void deletePreset(@NonNull Preset preset);

    void createNewGeneration();

    interface UIImpl extends UI {
        void onPresetsFetched(@Nullable Preset pendingPreset, @NonNull List<Preset> presets);

        void onFailedToFetchPresets();

        void onLongRunningAction(boolean stopped);

        void onFailedToDeletePreset();

        void showConfirmLastAction();

        void showNewGeneration();

        void showEditPreset();
    }
}
