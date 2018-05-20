package com.stedi.randomimagegenerator.app.presenter.interfaces

import android.graphics.Bitmap
import android.support.annotation.WorkerThread
import com.stedi.randomimagegenerator.ImageParams
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI
import java.io.File

interface GenerationPresenter : Presenter<GenerationPresenter.UIImpl> {

    fun startGeneration(preset: Preset)

    fun cancelGeneration()

    interface UIImpl : UI {
        @WorkerThread
        fun imageGenerated(imageParams: ImageParams, bitmap: Bitmap) {}

        @WorkerThread
        fun imageSaved(bitmap: Bitmap, file: File) {}

        fun onResult(generatedCount: Int, failedCount: Int)

        fun onFinishGeneration()

        fun onGenerationFailed()
    }
}