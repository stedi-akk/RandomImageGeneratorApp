package com.stedi.randomimagegenerator.app.presenter.interfaces

import com.stedi.randomimagegenerator.generators.ColoredNoiseGenerator

interface ColoredNoiseParamsPresenter : SimpleIntegerParamsPresenter {

    fun setOrientation(orientation: ColoredNoiseGenerator.Orientation)

    fun setType(type: ColoredNoiseGenerator.Type)

    interface UIImpl : SimpleIntegerParamsPresenter.UIImpl {
        fun showOrientation(orientation: ColoredNoiseGenerator.Orientation)

        fun showType(type: ColoredNoiseGenerator.Type)
    }
}