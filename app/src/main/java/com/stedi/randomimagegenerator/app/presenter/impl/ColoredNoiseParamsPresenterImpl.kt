package com.stedi.randomimagegenerator.app.presenter.impl

import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.model.data.generatorparams.ColoredNoiseParams
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams
import com.stedi.randomimagegenerator.app.presenter.interfaces.ColoredNoiseParamsPresenter
import com.stedi.randomimagegenerator.generators.ColoredNoiseGenerator
import javax.inject.Inject

class ColoredNoiseParamsPresenterImpl @Inject constructor(
        private val pendingPreset: PendingPreset) : ColoredNoiseParamsPresenter {

    private val candidate: Preset
        get() = pendingPreset.getCandidate()
                ?: throw IllegalStateException("pending preset candidate must not be null")

    private var ui: ColoredNoiseParamsPresenter.UIImpl? = null

    private lateinit var params: ColoredNoiseParams

    override fun onAttach(ui: ColoredNoiseParamsPresenter.UIImpl) {
        this.ui = ui
        val currentParams = candidate.getGeneratorParams()
        if (currentParams is EffectGeneratorParams) {
            params = currentParams.target as ColoredNoiseParams
        } else {
            params = currentParams as ColoredNoiseParams
        }
    }

    override fun onDetach() {
        this.ui = null
    }

    override fun getValues() {
        ui?.showOrientation(params.noiseOrientation)
        ui?.showType(params.noiseType)
    }

    override fun setOrientation(orientation: ColoredNoiseGenerator.Orientation) {
        params.noiseOrientation = orientation
    }

    override fun setType(type: ColoredNoiseGenerator.Type) {
        params.noiseType = type
    }
}