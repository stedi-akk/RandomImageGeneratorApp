package com.stedi.randomimagegenerator.app.presenter.impl

import com.squareup.otto.Subscribe
import com.stedi.randomimagegenerator.app.di.DefaultScheduler
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.other.logger.Logger
import com.stedi.randomimagegenerator.app.presenter.interfaces.HomePresenter
import rx.Scheduler
import rx.Single
import java.io.Serializable
import javax.inject.Inject

class HomePresenterImpl @Inject constructor(
        private val presetRepository: PresetRepository,
        private val pendingPreset: PendingPreset,
        @DefaultScheduler private val subscribeOn: Scheduler,
        @UiScheduler private val observeOn: Scheduler,
        private val bus: CachedBus,
        private val logger: Logger) : HomePresenter {

    private var ui: HomePresenter.UIImpl? = null
    private var fetchInProgress: Boolean = false

    private var deletePresetId: Int = 0
    private var generatePresetId: Int = 0

    class FetchPresetsEvent(val presets: List<Preset> = emptyList(), val throwable: Throwable? = null)
    class DeletePresetEvent(val preset: Preset?, val throwable: Throwable? = null)

    override fun onAttach(ui: HomePresenter.UIImpl) {
        this.ui = ui
        bus.register(this)
    }

    override fun onDetach() {
        bus.unregister(this)
        ui = null
    }

    override fun fetchPresets() {
        if (fetchInProgress) {
            return
        }
        fetchInProgress = true

        logger.log(this, "fetching presets")
        Single.fromCallable { presetRepository.getAll() }
                .subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .subscribe({
                    bus.post(FetchPresetsEvent(it))
                }, { t ->
                    bus.post(FetchPresetsEvent(throwable = t))
                })
    }

    override fun editPreset(preset: Preset) {
        pendingPreset.prepareCandidateFrom(preset)
        ui?.showEditPreset()
    }

    override fun newPreset() {
        pendingPreset.newDefaultCandidate()
        ui?.showCreatePreset()
    }

    override fun deletePreset(preset: Preset) {
        if (deletePresetId != 0) {
            logger.log(this, "ignoring deletePreset, because last deletePreset is not confirmed/canceled")
            return
        }

        logger.log(this, "deletePreset $preset")
        if (pendingPreset.getPreset() === preset) {
            pendingPreset.clearPreset()
            ui?.onPresetDeleted(preset)
            return
        }

        deletePresetId = preset.id
        ui?.showConfirmDeletePreset()
    }

    override fun confirmDeletePreset(confirm: Boolean) {
        if (deletePresetId == 0) {
            return
        }

        if (!confirm) {
            logger.log(this, "cancelDeletePreset")
            deletePresetId = 0
            return
        }

        logger.log(this, "confirmDeletePreset")
        val presetId = deletePresetId
        deletePresetId = 0

        Single.fromCallable {
            val preset = presetRepository.get(presetId)
            presetRepository.remove(presetId)
            return@fromCallable preset
        }.subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .subscribe({
                    bus.post(DeletePresetEvent(it))
                }, { t ->
                    bus.post(DeletePresetEvent(null, t))
                })
    }

    override fun startGeneration(preset: Preset) {
        if (generatePresetId != 0) {
            logger.log(this, "ignoring startGeneration, because last startGeneration is not confirmed/canceled")
            return
        }

        logger.log(this, "startGeneration $preset")
        generatePresetId = preset.id
        ui?.showConfirmGeneratePreset()
    }

    override fun confirmStartGeneration(confirm: Boolean) {
        if (generatePresetId == 0) {
            return
        }

        if (!confirm) {
            logger.log(this, "cancelStartGeneration")
            generatePresetId = 0
            return
        }

        logger.log(this, "confirmStartGeneration")
        generatePresetId = 0
        ui?.showGenerationDialog()
    }

    @Subscribe
    fun onFetchPresetsEvent(event: FetchPresetsEvent) {
        logger.log(this, "onFetchPresetsEvent")

        if (!fetchInProgress) {
            logger.log(this, "ignoring event from not retained presenter!")
            return
        }

        fetchInProgress = false

        if (ui == null) {
            logger.log(this, "onFetchPresetsEvent when ui == null")
            return
        }

        event.throwable?.apply {
            logger.log(this@HomePresenterImpl, this)
            ui?.onFailedToFetchPresets()
        } ?: ui?.onPresetsFetched(pendingPreset.getPreset(), event.presets)
    }

    @Subscribe
    fun onDeletePresetEvent(event: DeletePresetEvent) {
        logger.log(this, "onDeletePresetEvent")

        if (ui == null) {
            logger.log(this, "onDeletePresetEvent when ui == null")
            return
        }

        if (event.throwable != null || event.preset == null) {
            logger.log(this, "failed to delete preset", event.throwable)
            ui?.onFailedToDeletePreset()
        } else {
            ui?.onPresetDeleted(event.preset)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onRestore(state: Serializable) {
        (state as Array<Any>).apply {
            fetchInProgress = this[0] as Boolean
            deletePresetId = this[1] as Int
            generatePresetId = this[2] as Int
        }
    }

    override fun onRetain(): Serializable? {
        return arrayOf(fetchInProgress, deletePresetId, generatePresetId)
    }
}