package com.stedi.randomimagegenerator.app.presenter.impl

import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams
import com.stedi.randomimagegenerator.app.other.logger.Logger
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseGeneratorPresenter
import javax.inject.Inject

class ChooseGeneratorPresenterImpl @Inject constructor(
        private val pendingPreset: PendingPreset,
        private val logger: Logger) : ChooseGeneratorPresenter {

    private val candidate: Preset
        get() = pendingPreset.getCandidate()
                ?: throw IllegalStateException("pending preset candidate must not be null")

    private var ui: ChooseGeneratorPresenter.UIImpl? = null

    override fun onAttach(ui: ChooseGeneratorPresenter.UIImpl) {
        this.ui = ui
    }

    override fun onDetach() {
        this.ui = null
    }

    override fun getGeneratorTypes() {
        val selectedType: GeneratorType
        val selectedParams = candidate.getGeneratorParams()
        if (selectedParams is EffectGeneratorParams) {
            selectedType = selectedParams.target.getType()
        } else {
            selectedType = selectedParams.getType()
        }
        ui?.showTypes(GeneratorType.NON_EFFECT_TYPES, selectedType)
    }

    override fun chooseGeneratorType(type: GeneratorType) {
        val newParams = GeneratorParams.createDefaultParams(type)
        val currentParams = candidate.getGeneratorParams()
        if (currentParams is EffectGeneratorParams) {
            currentParams.setTargetParams(newParams)
        } else {
            candidate.setGeneratorParams(newParams)
        }
        logger.log(this, "chooseGeneratorType result = " + candidate.getGeneratorParams())
    }

    override fun editChoseGeneratorParams() {
        val currentParams = candidate.getGeneratorParams()
        if (currentParams is EffectGeneratorParams) {
            ui?.showEditGeneratorParams(currentParams.target.getType())
        } else {
            ui?.showEditGeneratorParams(currentParams.getType())
        }
    }
}