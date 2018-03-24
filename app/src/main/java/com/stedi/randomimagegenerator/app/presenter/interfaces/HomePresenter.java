package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.di.RigScheduler;
import com.stedi.randomimagegenerator.app.di.UiScheduler;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.logger.Logger;

import java.util.List;

import rx.Scheduler;

public abstract class HomePresenter extends GenerationPresenter<HomePresenter.UIImpl> {
    public enum Confirm {
        DELETE_PRESET,
        GENERATE_FROM_PRESET
    }

    protected HomePresenter(@NonNull @RigScheduler Scheduler subscribeOn,
                         @NonNull @UiScheduler Scheduler observeOn,
                         @NonNull CachedBus bus, @NonNull Logger logger) {
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

        void onPresetDeleted(@NonNull Preset preset);

        void onFailedToDeletePreset();

        void showConfirmLastAction(@NonNull Confirm confirm);

        void showEditPreset();
    }
}
