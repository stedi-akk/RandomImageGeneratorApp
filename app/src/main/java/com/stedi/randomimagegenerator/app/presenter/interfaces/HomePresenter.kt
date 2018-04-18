package com.stedi.randomimagegenerator.app.presenter.interfaces

import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.RetainedPresenter
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI

interface HomePresenter : RetainedPresenter<HomePresenter.UIImpl> {

    fun fetchPresets()

    fun editPreset(preset: Preset)

    fun newPreset()

    fun deletePreset(preset: Preset)

    fun confirmDeletePreset(confirm: Boolean)

    fun startGeneration(preset: Preset)

    fun confirmStartGeneration(confirm: Boolean)

    interface UIImpl : UI {
        fun onPresetsFetched(pendingPreset: Preset?, presets: List<Preset>)

        fun onFailedToFetchPresets()

        fun showConfirmDeletePreset()

        fun onPresetDeleted(preset: Preset)

        fun onFailedToDeletePreset()

        fun showConfirmGeneratePreset()

        fun showGenerationDialog()

        fun showEditPreset()

        fun showCreatePreset()
    }
}