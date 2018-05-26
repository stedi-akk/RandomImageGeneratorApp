package com.stedi.randomimagegenerator.app.presenter.impl

import android.graphics.Bitmap
import com.squareup.otto.Subscribe
import com.stedi.randomimagegenerator.app.di.DefaultScheduler
import com.stedi.randomimagegenerator.app.di.SaveFolder
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.other.LockedBus
import com.stedi.randomimagegenerator.app.presenter.interfaces.PreviewGenerationPresenter
import rx.Scheduler
import timber.log.Timber
import javax.inject.Inject

class PreviewGenerationPresenterImpl @Inject constructor(
        private val pendingPreset: PendingPreset,
        @SaveFolder private val saveFolder: String,
        @DefaultScheduler private val subscribeOn: Scheduler,
        @UiScheduler private val observeOn: Scheduler,
        private val bus: LockedBus) : PreviewGenerationPresenter {

    private val PREVIEW_FOLDER_NAME = "PREVIEW"

    private val candidate: Preset
        get() = pendingPreset.getCandidate()
                ?: throw IllegalStateException("pending preset candidate must not be null")

    private var ui: PreviewGenerationPresenter.UIImpl? = null
    private var saveInProgress: Boolean = false

    class ImageSaveEvent(val throwable: Throwable? = null)

    init {
        bus.register(this)
    }

    override fun onAttach(ui: PreviewGenerationPresenter.UIImpl) {
        this.ui = ui
    }

    override fun onDetach() {
        this.ui = null
    }

    override fun onDestroy() {
        bus.unregister(this)
    }

    override fun getPreset(): Preset {
        return candidate.makeCopy()
    }

    override fun saveImage(bitmap: Bitmap) {
        if (saveInProgress) {
            return
        }
        saveInProgress = true
        // TODO
    }

    @Subscribe
    fun onImageSaveEvent(event: PreviewGenerationPresenterImpl.ImageSaveEvent) {
        Timber.d("onImageSaveEvent")
        saveInProgress = false

        event.throwable?.apply {
            Timber.e(this)
            ui?.onImageFailedToSave()
        } ?: ui?.onImageSaved()
    }
}