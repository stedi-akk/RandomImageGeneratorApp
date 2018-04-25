package com.stedi.randomimagegenerator.app.presenter.impl

import android.os.Parcelable
import com.squareup.otto.Subscribe
import com.stedi.randomimagegenerator.app.di.DefaultScheduler
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository
import com.stedi.randomimagegenerator.app.other.LockedBus
import com.stedi.randomimagegenerator.app.presenter.interfaces.HomePresenter
import rx.Completable
import rx.Scheduler
import rx.Single
import timber.log.Timber
import java.io.Serializable
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

    private var deletePreset: Preset? = null
    private var generatePreset: Preset? = null

    class FetchPresetsEvent(val presets: List<Preset> = emptyList(), val throwable: Throwable? = null)
    class DeletePresetEvent(val preset: Preset, val throwable: Throwable? = null)

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
        if (deletePreset != null) {
            Timber.d("ignoring deletePreset, because last deletePreset is not finished")
            return
        }

        Timber.d("deletePreset $preset")
        if (pendingPreset.getPreset() === preset) {
            pendingPreset.clearPreset()
            ui?.onPresetDeleted(preset)
            return
        }

        deletePreset = preset
        ui?.showConfirmDeletePreset(preset)
    }

    override fun confirmDeletePreset(confirm: Boolean) {
        val deletePreset = deletePreset
        if (deletePreset == null) {
            return
        }

        if (!confirm) {
            Timber.d("cancelDeletePreset")
            this.deletePreset = null
            return
        }

        Timber.d("confirmDeletePreset")
        val presetId = deletePreset.id
        this.deletePreset = null
        deleteInProgress = true

        Completable.fromCallable {
            presetRepository.remove(presetId)
        }.subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .subscribe({
                    bus.post(DeletePresetEvent(deletePreset))
                }, { t ->
                    bus.post(DeletePresetEvent(deletePreset, t))
                })
    }

    override fun startGeneration(preset: Preset) {
        if (generatePreset != null) {
            Timber.d("ignoring startGeneration, because last startGeneration is not confirmed/canceled")
            return
        }

        Timber.d("startGeneration $preset")
        generatePreset = preset
        ui?.showConfirmGeneratePreset(preset)
    }

    override fun confirmStartGeneration(confirm: Boolean) {
        val generatePreset = generatePreset
        if (generatePreset == null) {
            return
        }

        if (!confirm) {
            Timber.d("cancelStartGeneration")
            this.generatePreset = null
            return
        }

        Timber.d("confirmStartGeneration")
        this.generatePreset = null
        ui?.showGenerationDialog(generatePreset)
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

        event.throwable?.apply {
            Timber.e(this)
            ui?.onFailedToDeletePreset()
        } ?: ui?.onPresetDeleted(event.preset)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onRestore(state: Serializable) {
        (state as Array<Parcelable?>).apply {
            deletePreset = this[0] as Preset?
            generatePreset = this[1] as Preset?
        }
    }

    override fun onRetain(): Serializable? {
        return arrayOf(deletePreset, generatePreset)
    }
}