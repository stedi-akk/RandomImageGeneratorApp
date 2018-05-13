package com.stedi.randomimagegenerator.app.presenter.impl

import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseEffectPresenter
import timber.log.Timber
import javax.inject.Inject

class ChooseEffectPresenterImpl @Inject constructor(
        private val pendingPreset: PendingPreset) : ChooseEffectPresenter {

    private val candidate: Preset
        get() = pendingPreset.getCandidate()
                ?: throw IllegalStateException("pending preset candidate must not be null")

    private var ui: ChooseEffectPresenter.UIImpl? = null

    override fun onAttach(ui: ChooseEffectPresenter.UIImpl) {
        this.ui = ui
    }

    override fun onDetach() {
        this.ui = null
    }

    override fun getEffectTypes() {
        val currentParams = candidate.getGeneratorParams()
        val effectType: GeneratorType?
        val targetType: GeneratorType
        if (currentParams is EffectGeneratorParams) {
            effectType = currentParams.getType()
            targetType = currentParams.target.getType()
        } else {
            effectType = null
            targetType = currentParams.getType()
        }
        ui?.showTypes(GeneratorType.EFFECT_TYPES, effectType, targetType)
    }

    override fun chooseEffectType(effectType: GeneratorType?) {
        val newParams: GeneratorParams
        var prevParams = candidate.getGeneratorParams()
        if (prevParams is EffectGeneratorParams) {
            prevParams = prevParams.target
        }
        if (effectType != null) {
            newParams = GeneratorParams.createDefaultEffectParams(effectType, prevParams)
        } else {
            newParams = prevParams
        }
        candidate.setGeneratorParams(newParams)
        Timber.d("chooseGeneratorType result = ${candidate.getGeneratorParams()}")
    }
}