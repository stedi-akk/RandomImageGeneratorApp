package com.stedi.randomimagegenerator.app.presenter.interfaces

import android.graphics.Bitmap
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI
import java.io.File

interface PreviewGenerationPresenter : Presenter<PreviewGenerationPresenter.UIImpl> {

    companion object {
        const val PREVIEW_FOLDER_NAME = "PREVIEW"
        const val PREVIEW_FILE_NAME_FORMAT = "rig_preview_%d.%s"
    }

    fun getPreset(): Preset

    fun saveImage(bitmap: Bitmap)

    interface UIImpl : UI {
        fun onImageSaved(file: File)

        fun onImageFailedToSave()
    }
}