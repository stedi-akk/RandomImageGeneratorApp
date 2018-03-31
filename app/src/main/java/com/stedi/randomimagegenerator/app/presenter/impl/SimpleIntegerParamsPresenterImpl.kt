package com.stedi.randomimagegenerator.app.presenter.impl

import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.SimpleIntegerParams
import com.stedi.randomimagegenerator.app.other.logger.Logger
import com.stedi.randomimagegenerator.app.presenter.interfaces.SimpleIntegerParamsPresenter

class SimpleIntegerParamsPresenterImpl(private val pendingPreset: PendingPreset,
                                       private val logger: Logger) : SimpleIntegerParamsPresenter {

    private val candidate: Preset
        get() = pendingPreset.getCandidate()
                ?: throw IllegalStateException("pending preset candidate must not be null")

    private var ui: SimpleIntegerParamsPresenter.UIImpl? = null

    private lateinit var params: SimpleIntegerParams

    override fun onAttach(ui: SimpleIntegerParamsPresenter.UIImpl) {
        logger.log(this, "onAttach")
        this.ui = ui
        val currentParams = candidate.getGeneratorParams()
        if (currentParams is EffectGeneratorParams) {
            params = currentParams.target as SimpleIntegerParams
        } else {
            params = currentParams as SimpleIntegerParams
        }
    }

    override fun onDetach() {
        logger.log(this, "onDetach")
        this.ui = null
    }

    override fun getValues() {
        params.getValue()?.apply {
            ui?.showValue(this)
        } ?: ui?.showRandomValue()
    }

    override fun canBeRandom() = params.canBeRandom()

    override fun setRandomValue() {
        if (!params.canBeRandom()) {
            throw IllegalStateException("setRandomValue called when canBeRandom is false")
        }
        logger.log(this, "setRandomValue")
        params.setRandomValue()
    }

    override fun setValue(value: Int): Boolean {
        if (value < 1) {
            ui?.showErrorIncorrectValue()
            return false
        }
        logger.log(this, "setValue $value")
        params.setValue(value)
        return true
    }
}