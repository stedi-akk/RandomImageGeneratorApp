package com.stedi.randomimagegenerator.app.presenter.impl

import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSizeAndCountPresenter
import timber.log.Timber
import javax.inject.Inject

class ChooseSizeAndCountPresenterImpl @Inject constructor(
        private val pendingPreset: PendingPreset) : ChooseSizeAndCountPresenter {

    private val candidate: Preset
        get() = pendingPreset.getCandidate()
                ?: throw IllegalStateException("pending preset candidate must not be null")

    private var ui: ChooseSizeAndCountPresenter.UIImpl? = null

    override fun onAttach(ui: ChooseSizeAndCountPresenter.UIImpl) {
        this.ui = ui
    }

    override fun onDetach() {
        this.ui = null
    }

    override fun getValues() {
        var showCount = true

        candidate.getWidthRange()?.apply {
            ui?.showWidthRange(this[0], this[1], this[2])
            showCount = false
        } ?: ui?.showWidth(candidate.getWidth())

        candidate.getHeightRange()?.apply {
            ui?.showHeightRange(this[0], this[1], this[2])
            showCount = false
        } ?: ui?.showHeight(candidate.getHeight())

        if (showCount) {
            ui?.showCount(candidate.getCount())
        }
    }

    override fun setCount(count: Int) {
        if (count < 1 || candidate.getWidthRange() != null || candidate.getHeightRange() != null) {
            ui?.onError(ChooseSizeAndCountPresenter.Error.INCORRECT_COUNT)
            return
        }
        candidate.setCount(count)
        Timber.d("after setCount: $candidate")
    }

    override fun setWidth(width: Int) {
        if (width < 1) {
            ui?.onError(ChooseSizeAndCountPresenter.Error.INCORRECT_WIDTH)
            return
        }
        candidate.setWidth(width)
        Timber.d("after setWidth: $candidate")
    }

    override fun setHeight(height: Int) {
        if (height < 1) {
            ui?.onError(ChooseSizeAndCountPresenter.Error.INCORRECT_HEIGHT)
            return
        }
        candidate.setHeight(height)
        Timber.d("after setHeight: $candidate")
    }

    override fun setWidthRange(from: Int, to: Int, step: Int) {
        if (from < 1 || to < 1 || step < 1) {
            ui?.onError(ChooseSizeAndCountPresenter.Error.INCORRECT_WIDTH_RANGE)
            return
        }
        candidate.setWidthRange(from, to, step)
        Timber.d("after setWidthRange: $candidate")
    }

    override fun setHeightRange(from: Int, to: Int, step: Int) {
        if (from < 1 || to < 1 || step < 1) {
            ui?.onError(ChooseSizeAndCountPresenter.Error.INCORRECT_HEIGHT_RANGE)
            return
        }
        candidate.setHeightRange(from, to, step)
        Timber.d("after setHeightRange: $candidate")
    }
}