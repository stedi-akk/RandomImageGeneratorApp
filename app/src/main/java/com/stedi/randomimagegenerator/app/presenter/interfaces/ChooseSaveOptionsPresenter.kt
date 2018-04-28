package com.stedi.randomimagegenerator.app.presenter.interfaces

import android.graphics.Bitmap
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI

interface ChooseSaveOptionsPresenter : Presenter<ChooseSaveOptionsPresenter.UIImpl> {
    fun setQualityFormat(format: Bitmap.CompressFormat)

    fun setQualityValue(value: Int)

    fun getValues()

    interface UIImpl : UI {
        fun showQualityFormat(format: Bitmap.CompressFormat)

        fun showQualityValue(value: Int)

        fun onIncorrectQualityValue()
    }
}