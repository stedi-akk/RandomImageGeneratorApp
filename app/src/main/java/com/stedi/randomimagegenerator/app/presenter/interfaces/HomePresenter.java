package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.RetainedPresenter;

import java.util.List;

public interface HomePresenter extends RetainedPresenter<HomePresenter.UIImpl> {
    void fetchPresets();

    void editPreset(@NonNull Preset preset);

    void generateFromPreset(@NonNull Preset preset);

    void confirmLastAction();

    void cancelLastAction();

    void openFolder(@NonNull Preset preset);

    void deletePreset(@NonNull Preset preset);

    interface UIImpl extends RetainedPresenter.RetainedUI {
        void onPresetsFetched(@Nullable Preset pendingPreset, @NonNull List<Preset> presets);

        void onFailedToFetchPresets();

        void onFailedToDeletePreset();

        void showConfirmLastAction();

        void showEditPreset();
    }
}
