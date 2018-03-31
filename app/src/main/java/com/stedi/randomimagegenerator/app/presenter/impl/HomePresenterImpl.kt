package com.stedi.randomimagegenerator.app.presenter.impl

import android.annotation.SuppressLint
import com.squareup.otto.Subscribe
import com.stedi.randomimagegenerator.app.di.DefaultScheduler
import com.stedi.randomimagegenerator.app.di.RigScheduler
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.other.ChainSerializable
import com.stedi.randomimagegenerator.app.other.logger.Logger
import com.stedi.randomimagegenerator.app.presenter.interfaces.HomePresenter
import rx.Scheduler
import rx.Single
import java.io.Serializable

class HomePresenterImpl(
        private val presetRepository: PresetRepository,
        private val pendingPreset: PendingPreset,
        @DefaultScheduler
        private val subscribeOn: Scheduler,
        @RigScheduler
        superSubscribeOn: Scheduler,
        @UiScheduler
        private val observeOn: Scheduler,
        private val bus: CachedBus,
        private val logger: Logger) : HomePresenter(superSubscribeOn, observeOn, bus, logger) {

    private var ui: HomePresenter.UIImpl? = null
    private var fetchInProgress: Boolean = false

    private var lastActionConfirm: HomePresenter.Confirm? = null
    private var lastActionPresetId: Int = 0

    class FetchPresetsEvent(val presets: List<Preset>?, val throwable: Throwable? = null)
    class DeletePresetEvent(val preset: Preset?, val throwable: Throwable? = null)

    override fun onAttach(ui: HomePresenter.UIImpl) {
        super.onAttach(ui)
        this.ui = ui
        bus.register(this)
    }

    override fun onDetach() {
        super.onDetach()
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
                    bus.post(FetchPresetsEvent(null, t))
                })
    }

    override fun editPreset(preset: Preset) {
        pendingPreset.prepareCandidateFrom(preset)
        ui?.showEditPreset()
    }

    override fun confirmLastAction() {
        logger.log(this, "confirmLastAction $lastActionConfirm")

        if (lastActionConfirm == Confirm.DELETE_PRESET) {
            deletePreset(lastActionPresetId)
        } else if (lastActionConfirm == HomePresenter.Confirm.GENERATE_FROM_PRESET) {
            startGeneration(lastActionPresetId)
        }

        lastActionConfirm = null
        lastActionPresetId = 0
    }

    override fun cancelLastAction() {
        logger.log(this, "cancelLastAction $lastActionConfirm")
        lastActionConfirm = null
        lastActionPresetId = 0
    }

    override fun deletePreset(preset: Preset) {
        if (lastActionConfirm != null) {
            logger.log(this, "ignoring deletePreset, because last action $lastActionConfirm is not confirmed/canceled")
            return
        }
        logger.log(this, "deletePreset $preset")
        if (pendingPreset.get() === preset) {
            pendingPreset.clear()
            ui?.onPresetDeleted(preset)
            return
        }
        lastActionConfirm = HomePresenter.Confirm.DELETE_PRESET
        lastActionPresetId = preset.id
        ui?.showConfirmLastAction(lastActionConfirm!!)
    }

    override fun startGeneration(preset: Preset) {
        if (lastActionConfirm != null) {
            logger.log(this, "ignoring startGeneration, because last action $lastActionConfirm is not confirmed/canceled")
            return
        }
        logger.log(this, "startGeneration $preset")
        lastActionConfirm = HomePresenter.Confirm.GENERATE_FROM_PRESET
        lastActionPresetId = preset.id
        ui?.showConfirmLastAction(lastActionConfirm!!)
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
        } ?: ui?.onPresetsFetched(pendingPreset.get(), event.presets!!)
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

    override fun onRestore(state: Serializable) {
        var chain = state as ChainSerializable
        super.onRestore(chain.get()!!)
        chain = chain.getChain()!!
        fetchInProgress = chain.get() as Boolean
        chain = chain.getChain()!!
        lastActionConfirm = chain.get() as HomePresenter.Confirm?
        chain = chain.getChain()!!
        lastActionPresetId = chain.get() as Int
    }

    override fun onRetain(): Serializable? {
        return ChainSerializable(super.onRetain()).apply {
            addChain(fetchInProgress).addChain(lastActionConfirm).addChain(lastActionPresetId)
        }
    }

    private fun deletePreset(presetId: Int) {
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

    @SuppressLint("MissingPermission")
    private fun startGeneration(presetId: Int) {
        Single.fromCallable { presetRepository.get(presetId) }
                .subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .subscribe({
                    it?.apply { super.startGeneration(this) }
                }, { t ->
                    logger.log(this, t)
                })
    }
}