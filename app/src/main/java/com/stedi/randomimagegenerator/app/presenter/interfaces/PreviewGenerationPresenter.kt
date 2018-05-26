package com.stedi.randomimagegenerator.app.presenter.interfaces

import android.graphics.Bitmap
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI

interface PreviewGenerationPresenter : Presenter<PreviewGenerationPresenter.UIImpl> {

    fun getPreset(): Preset

    fun saveImage(bitmap: Bitmap)

    interface UIImpl : UI {
        fun onImageSaved()

        fun onImageFailedToSave()
    }
}