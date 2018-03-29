package com.stedi.randomimagegenerator.app.presenter.interfaces

import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI

interface ChooseGeneratorPresenter : Presenter<ChooseGeneratorPresenter.UIImpl> {
    fun getGeneratorTypes()

    fun chooseGeneratorType(type: GeneratorType)

    fun editChoseGeneratorParams()

    interface UIImpl : UI {
        fun showTypes(types: Array<GeneratorType>, selectedType: GeneratorType)

        fun showEditGeneratorParams(type: GeneratorType)
    }
}