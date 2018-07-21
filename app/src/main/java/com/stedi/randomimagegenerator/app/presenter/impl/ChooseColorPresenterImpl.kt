package com.stedi.randomimagegenerator.app.presenter.impl

import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseColorPresenter
import javax.inject.Inject

class ChooseColorPresenterImpl @Inject constructor(
        private val pendingPreset: PendingPreset) : ChooseColorPresenter {

    private val candidate: Preset
        get() = pendingPreset.getCandidate()
                ?: throw IllegalStateException("pending preset candidate must not be null")

    private var ui: ChooseColorPresenter.UIImpl? = null

    override fun onAttach(ui: ChooseColorPresenter.UIImpl) {
        this.ui = ui
        ui.showColorRange(candidate.getColorFrom(), candidate.getColorTo())
        ui.showColorExtras(candidate.useLightColor, candidate.useDarkColor, candidate.isGrayscale)
    }

    override fun onDetach() {
        this.ui = null
    }

    override fun setColorRange(colorFrom: Int, colorTo: Int) {
        candidate.setColorFrom(colorFrom)
        candidate.setColorTo(colorTo)
    }

    override fun setColorExtras(useLightColor: Boolean, useDarkColor: Boolean, isGrayscale: Boolean) {
        candidate.useLightColor = useLightColor
        candidate.useDarkColor = useDarkColor
        candidate.isGrayscale = isGrayscale
    }
}