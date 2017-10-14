package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.di.qualifiers.RigScheduler;
import com.stedi.randomimagegenerator.app.di.qualifiers.UiScheduler;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.logger.Logger;

import rx.Scheduler;

public abstract class ApplyGenerationPresenter extends GenerationPresenter<ApplyGenerationPresenter.UIImpl> {
    protected ApplyGenerationPresenter(@NonNull @RigScheduler Scheduler subscribeOn,
                                    @NonNull @UiScheduler Scheduler observeOn,
                                    @NonNull CachedBus bus, @NonNull Logger logger) {
        super(subscribeOn, observeOn, bus, logger);
    }

    @NonNull
    public abstract Preset getPreset();

    public abstract boolean isPresetNewOrChanged();

    public abstract void savePreset(@NonNull String name);

    public interface UIImpl extends GenerationPresenter.UIImpl {
        void onPresetSaved();

        void failedToSavePreset();
    }
}
