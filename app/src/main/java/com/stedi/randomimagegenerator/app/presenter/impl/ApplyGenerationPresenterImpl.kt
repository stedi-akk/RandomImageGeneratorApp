package com.stedi.randomimagegenerator.app.presenter.impl

import com.squareup.otto.Subscribe
import com.stedi.randomimagegenerator.app.di.DefaultScheduler
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository
import com.stedi.randomimagegenerator.app.other.LockedBus
import com.stedi.randomimagegenerator.app.presenter.interfaces.ApplyGenerationPresenter
import rx.Completable
import rx.Scheduler
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class ApplyGenerationPresenterImpl @Inject constructor(
        private val pendingPreset: PendingPreset,
        private val presetRepository: PresetRepository,
        private val generationPath: String,
        private val defaultName: String,
        @DefaultScheduler private val subscribeOn: Scheduler,
        @UiScheduler private val observeOn: Scheduler,
        private val bus: LockedBus) : ApplyGenerationPresenter {

    private val UNSAVED_FOLDER_NAME = "0"

    private val candidate: Preset
        get() = pendingPreset.getCandidate()
                ?: throw IllegalStateException("pending preset candidate must not be null")

    private var ui: ApplyGenerationPresenter.UIImpl? = null
    private var saveInProgress: Boolean = false

    class OnPresetSaveEvent(val throwable: Throwable? = null)

    init {
        bus.register(this)
    }

    override fun onAttach(ui: ApplyGenerationPresenter.UIImpl) {
        this.ui = ui
    }

    override fun onDetach() {
        this.ui = null
    }

    override fun onDestroy() {
        bus.unregister(this)
    }

    override fun getPreset(): Preset {
        val preset = candidate.makeCopy()
        if (pendingPreset.isCandidateNew()) {
            preset.name = defaultName
            preset.pathToSave = File(generationPath, UNSAVED_FOLDER_NAME).path
        }
        return preset
    }

    override fun isPresetNew() = pendingPreset.isCandidateNew()

    override fun isPresetChanged() = pendingPreset.isCandidateChanged()

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
            preset.pathToSave = File(generationPath, preset.id.toString()).path
            presetRepository.save(preset)
        }.subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .subscribe({
                    if (candidate === pendingPreset.getPreset()) {
                        pendingPreset.clearPreset()
                    }
                    bus.post(OnPresetSaveEvent())
                }, { t ->
                    preset.name = originalName
                    preset.timestamp = originalTimestamp
                    preset.pathToSave = originalSavePath
                    bus.post(OnPresetSaveEvent(t))
                })
    }

    override fun startGeneration() {
        if (pendingPreset.isCandidateNew() || pendingPreset.isCandidateChanged()) {
            candidate.clearIds()
            candidate.name = defaultName
            candidate.pathToSave = File(generationPath, UNSAVED_FOLDER_NAME).path
            candidate.timestamp = System.currentTimeMillis()
            pendingPreset.applyCandidate()
        }
        ui?.showGenerationDialog(candidate)
    }

    @Subscribe
    fun onPresetSaveEvent(event: OnPresetSaveEvent) {
        Timber.d("onPresetSaveEvent")
        saveInProgress = false

        event.throwable?.apply {
            Timber.e(this)
            ui?.failedToSavePreset()
        } ?: ui?.onPresetSaved()
    }
}