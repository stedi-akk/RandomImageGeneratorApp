package com.stedi.randomimagegenerator.app.presenter.impl

import com.squareup.otto.Subscribe
import com.stedi.randomimagegenerator.app.di.DefaultScheduler
import com.stedi.randomimagegenerator.app.di.RootSavePath
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.other.logger.Logger
import com.stedi.randomimagegenerator.app.presenter.interfaces.ApplyGenerationPresenter
import rx.Completable
import rx.Scheduler
import java.io.File
import java.io.Serializable
import javax.inject.Inject

class ApplyGenerationPresenterImpl @Inject constructor(
        private val pendingPreset: PendingPreset,
        private val presetRepository: PresetRepository,
        @RootSavePath private val rootSavePath: String,
        @DefaultScheduler private val subscribeOn: Scheduler,
        @UiScheduler private val observeOn: Scheduler,
        private val bus: CachedBus,
        private val logger: Logger) : ApplyGenerationPresenter(subscribeOn, observeOn, bus, logger) {

    private val candidate: Preset
        get() = pendingPreset.getCandidate()
                ?: throw IllegalStateException("pending preset candidate must not be null")

    private var ui: ApplyGenerationPresenter.UIImpl? = null
    private var saveInProgress: Boolean = false

    class OnPresetSaveEvent(val throwable: Throwable? = null)

    override fun onAttach(ui: ApplyGenerationPresenter.UIImpl) {
        super.onAttach(ui)
        this.ui = ui
        bus.register(this)
    }

    override fun onDetach() {
        super.onDetach()
        bus.unregister(this)
        this.ui = null
    }

    override fun getPreset() = candidate

    override fun isPresetNewOrChanged() = pendingPreset.isCandidateNewOrChanged()

    override fun savePreset(name: String) {
        if (saveInProgress) {
            return
        }
        saveInProgress = true

        val preset = candidate
        val originalName = preset.name
        val originalTimestamp = preset.timestamp
        val originalSavePath = preset.pathToSave

        Completable.fromCallable {
            preset.name = name
            preset.timestamp = System.currentTimeMillis()
            presetRepository.save(preset)
            preset.pathToSave = rootSavePath + File.separator + preset.id
            presetRepository.save(preset)
        }.subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .subscribe({
                    bus.post(OnPresetSaveEvent())
                }, { t ->
                    preset.name = originalName
                    preset.timestamp = originalTimestamp
                    preset.pathToSave = originalSavePath
                    bus.post(OnPresetSaveEvent(t))
                })
    }

    @Subscribe
    fun onPresetSaveEvent(event: OnPresetSaveEvent) {
        logger.log(this, "onPresetSaveEvent")
        saveInProgress = false

        if (event.throwable == null) {
            if (candidate === pendingPreset.get()) {
                pendingPreset.clear()
            }
            pendingPreset.candidateSaved()
        }

        if (ui == null) {
            logger.log(this, "onPresetSaveEvent when ui == null")
            return
        }

        event.throwable?.apply {
            logger.log(this@ApplyGenerationPresenterImpl, this)
            ui?.failedToSavePreset()
        } ?: ui?.onPresetSaved()
    }

    @SuppressWarnings("MissingPermission")
    override fun startGeneration(preset: Preset) {
        if (preset !== candidate) {
            throw IllegalArgumentException("candidate preset is required")
        }

        if (pendingPreset.isCandidateNewOrChanged()) {
            pendingPreset.applyCandidate()
        }

        super.startGeneration(candidate)
    }

    override fun onRestore(state: Serializable) {
        (state as Array<Serializable>).apply {
            super.onRestore(this[0])
            saveInProgress = this[1] as Boolean
        }
    }

    override fun onRetain(): Serializable? {
        return arrayOf(super.onRetain(), saveInProgress)
    }
}