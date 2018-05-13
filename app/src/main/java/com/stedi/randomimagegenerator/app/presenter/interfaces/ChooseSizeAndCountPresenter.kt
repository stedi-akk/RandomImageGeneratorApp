package com.stedi.randomimagegenerator.app.presenter.interfaces

import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI

interface ChooseSizeAndCountPresenter : Presenter<ChooseSizeAndCountPresenter.UIImpl> {
    enum class Error {
        INCORRECT_WIDTH,
        INCORRECT_HEIGHT,
        INCORRECT_WIDTH_RANGE,
        INCORRECT_HEIGHT_RANGE,
        INCORRECT_COUNT
    }

    fun getValues()

    fun setWidth(width: Int)

    fun setHeight(height: Int)

    fun setWidthRange(from: Int, to: Int, step: Int)

    fun setHeightRange(from: Int, to: Int, step: Int)

    fun setCount(count: Int)

    interface UIImpl : UI {
        fun showWidth(width: Int)

        fun showHeight(height: Int)

        fun showWidthRange(from: Int, to: Int, step: Int)

        fun showHeightRange(from: Int, to: Int, step: Int)

        fun showCount(count: Int)

        fun onError(error: Error)
    }
}