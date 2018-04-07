package com.stedi.randomimagegenerator.app.presenter.impl

import android.graphics.Bitmap
import com.stedi.randomimagegenerator.Quality
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.other.logger.Logger
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSaveOptionsPresenter
import javax.inject.Inject

class ChooseSaveOptionsPresenterImpl @Inject constructor(
        private val pendingPreset: PendingPreset,
        private val logger: Logger) : ChooseSaveOptionsPresenter {

    private val candidate: Preset
        get() = pendingPreset.getCandidate()
                ?: throw IllegalStateException("pending preset candidate must not be null")

    private var ui: ChooseSaveOptionsPresenter.UIImpl? = null

    override fun onAttach(ui: ChooseSaveOptionsPresenter.UIImpl) {
        this.ui = ui
    }

    override fun onDetach() {
        this.ui = null
    }

    override fun getData() {
        ui?.showQualityFormat(candidate.getQuality().format)
        ui?.showQualityValue(candidate.getQuality().qualityValue)
    }

    override fun setQualityFormat(format: Bitmap.CompressFormat) {
        candidate.setQuality(Quality(format, candidate.getQuality().qualityValue))
        logger.log(this, "after setQualityFormat " + candidate.getQuality())
    }

    override fun setQualityValue(value: Int) {
        if (value < 0 || value > 100) {
            ui?.onIncorrectQualityValue()
            return
        }
        candidate.setQuality(Quality(candidate.getQuality().format, value))
        logger.log(this, "after setQualityValue " + candidate.getQuality())
    }
}