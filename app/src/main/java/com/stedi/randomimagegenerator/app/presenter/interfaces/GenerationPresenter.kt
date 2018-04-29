package com.stedi.randomimagegenerator.app.presenter.interfaces

import android.support.annotation.WorkerThread
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI
import java.io.File

interface GenerationPresenter : Presenter<GenerationPresenter.UIImpl> {

    fun startGeneration(preset: Preset)

    interface UIImpl : UI {
        @WorkerThread
        fun imageSaved(imageFile: File) {}

        fun onResult(generatedCount: Int, failedCount: Int)

        fun onFinishGeneration()

        fun onGenerationFailed()
    }
}