package com.stedi.randomimagegenerator.app.presenter.interfaces

import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI

interface ApplyGenerationPresenter : Presenter<ApplyGenerationPresenter.UIImpl> {

    fun getPreset(): Preset

    fun isPresetNew(): Boolean

    fun isPresetChanged(): Boolean

    fun savePreset(name: String)

    fun startGeneration()

    interface UIImpl : UI {
        fun onPresetSaved()

        fun failedToSavePreset()

        fun showGenerationDialog(preset: Preset)
    }
}