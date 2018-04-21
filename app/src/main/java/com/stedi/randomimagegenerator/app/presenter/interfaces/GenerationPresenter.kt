package com.stedi.randomimagegenerator.app.presenter.interfaces

import com.stedi.randomimagegenerator.ImageParams
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI
import java.io.File

interface GenerationPresenter : Presenter<GenerationPresenter.UIImpl> {

    fun startGeneration(preset: Preset)

    interface UIImpl : UI {
        fun onStartGeneration()

        fun onGenerated(imageParams: ImageParams, imageFile: File)

        fun onFailedToGenerate(imageParams: ImageParams)

        fun onGenerationUnknownError()

        fun onFinishGeneration()
    }
}