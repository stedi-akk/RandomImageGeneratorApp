package com.stedi.randomimagegenerator.app.presenter.interfaces

import com.stedi.randomimagegenerator.app.di.DefaultScheduler
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.other.logger.Logger
import rx.Scheduler

abstract class HomePresenter(
        @DefaultScheduler subscribeOn: Scheduler,
        @UiScheduler observeOn: Scheduler,
        bus: CachedBus,
        logger: Logger) : GenerationPresenter<HomePresenter.UIImpl>(subscribeOn, observeOn, bus, logger) {

    enum class Confirm {
        DELETE_PRESET,
        GENERATE_FROM_PRESET
    }

    abstract fun fetchPresets()

    abstract fun editPreset(preset: Preset)

    abstract fun confirmLastAction()

    abstract fun cancelLastAction()

    abstract fun deletePreset(preset: Preset)

    interface UIImpl : GenerationPresenter.UIImpl {
        fun onPresetsFetched(pendingPreset: Preset?, presets: List<Preset>)

        fun onFailedToFetchPresets()

        fun onPresetDeleted(preset: Preset)

        fun onFailedToDeletePreset()

        fun showConfirmLastAction(confirm: Confirm)

        fun showEditPreset()
    }
}