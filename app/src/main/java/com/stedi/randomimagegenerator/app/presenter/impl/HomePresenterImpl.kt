package com.stedi.randomimagegenerator.app.presenter.impl

import com.squareup.otto.Subscribe
import com.stedi.randomimagegenerator.app.di.DefaultScheduler
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository
import com.stedi.randomimagegenerator.app.other.LockedBus
import com.stedi.randomimagegenerator.app.presenter.interfaces.HomePresenter
import rx.Scheduler
import rx.Single
import timber.log.Timber
import javax.inject.Inject

class HomePresenterImpl @Inject constructor(
        private val presetRepository: PresetRepository,
        private val pendingPreset: PendingPreset,
        @DefaultScheduler private val subscribeOn: Scheduler,
        @UiScheduler private val observeOn: Scheduler,
        private val bus: LockedBus) : HomePresenter {

    private var ui: HomePresenter.UIImpl? = null
    private var fetchInProgress: Boolean = false
    private var deleteInProgress: Boolean = false

    private var deletePresetId: Int = 0
    private var generatePresetId: Int = 0

    class FetchPresetsEvent(val presets: List<Preset> = emptyList(), val throwable: Throwable? = null)
    class DeletePresetEvent(val preset: Preset?, val throwable: Throwable? = null)

    init {
        bus.register(this)
    }

    override fun onAttach(ui: HomePresenter.UIImpl) {
        this.ui = ui
    }

    override fun onDetach() {
        ui = null
    }

    override fun onDestroy() {
        bus.unregister(this)
    }

    override fun fetchPresets() {
        if (fetchInProgress) {
            return
        }
        fetchInProgress = true

        Timber.d("fetching presets")
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
            Timber.d("ignoring deletePreset, because last deletePreset is not finished")
            return
        }

        Timber.d("deletePreset $preset")
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
            Timber.d("cancelDeletePreset")
            deletePresetId = 0
            return
        }

        Timber.d("confirmDeletePreset")
        val presetId = deletePresetId
        deletePresetId = 0
        deleteInProgress = true

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
            Timber.d("ignoring startGeneration, because last startGeneration is not confirmed/canceled")
            return
        }

        Timber.d("startGeneration $preset")
        generatePresetId = preset.id
        ui?.showConfirmGeneratePreset()
    }

    override fun confirmStartGeneration(confirm: Boolean) {
        if (generatePresetId == 0) {
            return
        }

        if (!confirm) {
            Timber.d("cancelStartGeneration")
            generatePresetId = 0
            return
        }

        Timber.d("confirmStartGeneration")
        generatePresetId = 0
        ui?.showGenerationDialog()
    }

    @Subscribe
    fun onFetchPresetsEvent(event: FetchPresetsEvent) {
        if (!fetchInProgress) {
            Timber.d("ignoring FetchPresetsEvent from previous presenter")
            return
        }

        Timber.d("onFetchPresetsEvent")
        fetchInProgress = false

        if (ui == null) {
            Timber.d("onFetchPresetsEvent when ui == null")
            return
        }

        event.throwable?.apply {
            Timber.e(this)
            ui?.onFailedToFetchPresets()
        } ?: ui?.onPresetsFetched(pendingPreset.getPreset(), event.presets)
    }

    @Subscribe
    fun onDeletePresetEvent(event: DeletePresetEvent) {
        if (!deleteInProgress) {
            Timber.d("ignoring DeletePresetEvent from previous presenter")
            return
        }

        Timber.d("onDeletePresetEvent")
        deleteInProgress = false

        if (ui == null) {
            Timber.d("onDeletePresetEvent when ui == null")
            return
        }

        if (event.throwable != null || event.preset == null) {
            Timber.e(event.throwable, "failed to delete preset")
            ui?.onFailedToDeletePreset()
        } else {
            ui?.onPresetDeleted(event.preset)
        }
    }
}