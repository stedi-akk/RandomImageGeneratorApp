package com.stedi.randomimagegenerator.app.presenter.impl

import android.graphics.Bitmap
import com.squareup.otto.Subscribe
import com.stedi.randomimagegenerator.app.di.DefaultScheduler
import com.stedi.randomimagegenerator.app.di.SaveFolder
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.other.LockedBus
import com.stedi.randomimagegenerator.app.other.compressProxy
import com.stedi.randomimagegenerator.app.presenter.interfaces.PreviewGenerationPresenter
import com.stedi.randomimagegenerator.app.presenter.interfaces.PreviewGenerationPresenter.Companion.PREVIEW_FILE_NAME_FORMAT
import com.stedi.randomimagegenerator.app.presenter.interfaces.PreviewGenerationPresenter.Companion.PREVIEW_FOLDER_NAME
import rx.Scheduler
import rx.Single
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

// actual generation should be performed with GenerationPresenterImpl
class PreviewGenerationPresenterImpl @Inject constructor(
        private val pendingPreset: PendingPreset,
        @SaveFolder private val saveFolder: String,
        @DefaultScheduler private val subscribeOn: Scheduler,
        @UiScheduler private val observeOn: Scheduler,
        private val bus: LockedBus) : PreviewGenerationPresenter {

    private val candidate: Preset
        get() = pendingPreset.getCandidate()
                ?: throw IllegalStateException("pending preset candidate must not be null")

    private var ui: PreviewGenerationPresenter.UIImpl? = null
    private var saveInProgress: Boolean = false

    class ImageSaveEvent(val file: File? = null, val throwable: Throwable? = null)

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

        val folderToSave = File(saveFolder, PREVIEW_FOLDER_NAME)
        val fileName = String.format(PREVIEW_FILE_NAME_FORMAT, System.currentTimeMillis(), candidate.getQuality().fileExtension)
        val compressFormat = candidate.getQuality().format

        Single.fromCallable {
            if (!folderToSave.exists() && !folderToSave.mkdirs()) {
                throw IOException("preview path is not valid")
            }
            val file = File(folderToSave.path, fileName)
            FileOutputStream(file).use {
                // bitmap should be already compressed, therefore the passed quality is 100
                if (!bitmap.compressProxy(compressFormat, 100, it)) {
                    throw IOException("failed to save preview bitmap")
                }
            }
            return@fromCallable file
        }.subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .subscribe({
                    bus.post(ImageSaveEvent(file = it))
                }, { t ->
                    bus.post(ImageSaveEvent(throwable = t))
                })
    }

    @Subscribe
    fun onImageSaveEvent(event: PreviewGenerationPresenterImpl.ImageSaveEvent) {
        Timber.d("onImageSaveEvent")
        saveInProgress = false

        event.throwable?.apply {
            Timber.e(this)
            ui?.onImageFailedToSave()
        } ?: ui?.onImageSaved(event.file!!)
    }
}