package com.stedi.randomimagegenerator.app.presenter.impl

import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.other.logger.Logger
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationStepsPresenter
import javax.inject.Inject

class GenerationStepsPresenterImpl @Inject constructor(
        private val pendingPreset: PendingPreset,
        private val logger: Logger) : GenerationStepsPresenter {

    private var ui: GenerationStepsPresenter.UIImpl? = null

    override fun onAttach(ui: GenerationStepsPresenter.UIImpl) {
        this.ui = ui
    }

    override fun onDetach() {
        this.ui = null
    }

    override fun setIsNew(isNew: Boolean) {
        if (isNew) {
            pendingPreset.newDefaultCandidate()
            ui?.showFirstStep()
        } else {
            if (pendingPreset.getCandidate() == null) {
                throw IllegalStateException("pending preset candidate must not be null")
            }
            ui?.showFinishStep()
        }
    }

    override fun release() {
        logger.log(this, "release called")
        pendingPreset.killCandidate()
    }
}