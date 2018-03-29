package com.stedi.randomimagegenerator.app.presenter.interfaces

import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI
import com.stedi.randomimagegenerator.generators.ColoredNoiseGenerator

interface ColoredNoiseParamsPresenter : Presenter<ColoredNoiseParamsPresenter.UIImpl> {
    fun getValues()

    fun setOrientation(orientation: ColoredNoiseGenerator.Orientation)

    fun setType(type: ColoredNoiseGenerator.Type)

    interface UIImpl : UI {
        fun showOrientation(orientation: ColoredNoiseGenerator.Orientation)

        fun showType(type: ColoredNoiseGenerator.Type)
    }
}