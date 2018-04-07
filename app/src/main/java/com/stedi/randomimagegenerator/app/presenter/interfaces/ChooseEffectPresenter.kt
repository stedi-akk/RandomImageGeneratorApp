package com.stedi.randomimagegenerator.app.presenter.interfaces

import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.Presenter
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI

interface ChooseEffectPresenter : Presenter<ChooseEffectPresenter.UIImpl> {
    fun getEffectTypes()

    fun chooseEffectType(effectType: GeneratorType?)

    interface UIImpl : UI {
        fun showTypes(types: Array<GeneratorType>, selectedType: GeneratorType?, targetType: GeneratorType)
    }
}