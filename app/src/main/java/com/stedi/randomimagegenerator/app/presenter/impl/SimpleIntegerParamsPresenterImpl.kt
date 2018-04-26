package com.stedi.randomimagegenerator.app.presenter.impl

import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.SimpleIntegerParams
import com.stedi.randomimagegenerator.app.presenter.interfaces.SimpleIntegerParamsPresenter
import timber.log.Timber
import javax.inject.Inject

class SimpleIntegerParamsPresenterImpl @Inject constructor(
        private val pendingPreset: PendingPreset) : SimpleIntegerParamsPresenter {

    private val candidate: Preset
        get() = pendingPreset.getCandidate()
                ?: throw IllegalStateException("pending preset candidate must not be null")

    private var ui: SimpleIntegerParamsPresenter.UIImpl? = null

    private lateinit var params: SimpleIntegerParams

    override fun onAttach(ui: SimpleIntegerParamsPresenter.UIImpl) {
        this.ui = ui
        val currentParams = candidate.getGeneratorParams()
        if (currentParams is EffectGeneratorParams) {
            params = currentParams.target as SimpleIntegerParams
        } else {
            params = currentParams as SimpleIntegerParams
        }
    }

    override fun onDetach() {
        this.ui = null
    }

    override fun getValues() {
        params.getValue()?.apply {
            ui?.showValue(this)
        } ?: ui?.showRandomValue()
    }

    override fun isRandomValue() = params.canBeRandom()

    override fun setRandomValue() {
        if (!params.canBeRandom()) {
            throw IllegalStateException("setRandomValue called when isRandomValue is false")
        }
        Timber.d("setRandomValue")
        params.setRandomValue()
    }

    override fun setValue(value: Int): Boolean {
        if (value < 1) {
            ui?.showErrorIncorrectValue()
            return false
        }
        Timber.d("setValue $value")
        params.setValue(value)
        return true
    }
}