package com.stedi.randomimagegenerator.app.presenter.interfaces

import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI

interface GenerationStepsPresenter : Presenter<GenerationStepsPresenter.UIImpl> {
    fun setIsNew(isNew: Boolean)

    fun release()

    interface UIImpl : UI {
        fun showFirstStep()

        fun showFinishStep()
    }
}
