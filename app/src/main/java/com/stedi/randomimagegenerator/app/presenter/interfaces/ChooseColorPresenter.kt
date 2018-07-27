package com.stedi.randomimagegenerator.app.presenter.interfaces

import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI

interface ChooseColorPresenter : Presenter<ChooseColorPresenter.UIImpl> {
    fun setColorRange(colorFrom: Int, colorTo: Int)

    fun setColorExtras(useLightColor: Boolean, useDarkColor: Boolean, isGrayscale: Boolean)

    interface UIImpl : UI {
        fun showColorRange(colorFrom: Int, colorTo: Int)

        fun showColorExtras(useLightColor: Boolean, useDarkColor: Boolean, isGrayscale: Boolean)
    }
}