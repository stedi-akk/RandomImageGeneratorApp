package com.stedi.randomimagegenerator.app.presenter.interfaces

import com.stedi.randomimagegenerator.app.di.RigScheduler
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.other.logger.Logger
import rx.Scheduler

abstract class ApplyGenerationPresenter(
        @RigScheduler
        subscribeOn: Scheduler,
        @UiScheduler
        observeOn: Scheduler,
        bus: CachedBus,
        logger: Logger) : GenerationPresenter<ApplyGenerationPresenter.UIImpl>(subscribeOn, observeOn, bus, logger) {

    abstract fun getPreset(): Preset

    abstract fun isPresetNewOrChanged(): Boolean

    abstract fun savePreset(name: String)

    interface UIImpl : GenerationPresenter.UIImpl {
        fun onPresetSaved()

        fun failedToSavePreset()
    }
}