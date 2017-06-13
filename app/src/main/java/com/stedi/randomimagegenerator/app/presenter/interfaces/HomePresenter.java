package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.logger.Logger;

import java.util.List;

import rx.Scheduler;

public abstract class HomePresenter extends GenerationPresenter<HomePresenter.UIImpl> {
    public HomePresenter(@NonNull Scheduler subscribeOn, @NonNull Scheduler observeOn, @NonNull CachedBus bus, @NonNull Logger logger) {
        super(subscribeOn, observeOn, bus, logger);
    }

    public abstract void fetchPresets();

    public abstract void editPreset(@NonNull Preset preset);

    public abstract void confirmLastAction();

    public abstract void cancelLastAction();

    public abstract void deletePreset(@NonNull Preset preset);

    public interface UIImpl extends GenerationPresenter.UIImpl {
        void onPresetsFetched(@Nullable Preset pendingPreset, @NonNull List<Preset> presets);

        void onFailedToFetchPresets();

        void onFailedToDeletePreset();

        void showConfirmLastAction();

        void showEditPreset();
    }
}
