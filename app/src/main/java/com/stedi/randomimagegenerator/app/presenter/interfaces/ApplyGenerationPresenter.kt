package com.stedi.randomimagegenerator.app.presenter.interfaces

import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.RetainedPresenter
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI

interface ApplyGenerationPresenter : RetainedPresenter<ApplyGenerationPresenter.UIImpl> {

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