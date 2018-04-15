package com.stedi.randomimagegenerator.app.presenter.interfaces

import com.stedi.randomimagegenerator.app.di.DefaultScheduler
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.other.logger.Logger
import rx.Scheduler

abstract class ApplyGenerationPresenter(
        @DefaultScheduler subscribeOn: Scheduler,
        @UiScheduler observeOn: Scheduler,
        bus: CachedBus,
        logger: Logger) : GenerationPresenter<ApplyGenerationPresenter.UIImpl>(subscribeOn, observeOn, bus, logger) {

    abstract fun getPreset(): Preset

    abstract fun isPresetNew(): Boolean

    abstract fun isPresetChanged(): Boolean

    abstract fun savePreset(name: String)

    interface UIImpl : GenerationPresenter.UIImpl {
        fun onPresetSaved()

        fun failedToSavePreset()
    }
}