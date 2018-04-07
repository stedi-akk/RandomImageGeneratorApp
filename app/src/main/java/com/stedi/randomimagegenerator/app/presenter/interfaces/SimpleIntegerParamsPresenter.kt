package com.stedi.randomimagegenerator.app.presenter.interfaces

import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI

interface SimpleIntegerParamsPresenter : Presenter<SimpleIntegerParamsPresenter.UIImpl> {
    fun getValues()

    fun canBeRandom(): Boolean

    fun setRandomValue()

    fun setValue(value: Int): Boolean

    interface UIImpl : UI {
        fun showRandomValue()

        fun showValue(value: Int)

        fun showErrorIncorrectValue()
    }
}