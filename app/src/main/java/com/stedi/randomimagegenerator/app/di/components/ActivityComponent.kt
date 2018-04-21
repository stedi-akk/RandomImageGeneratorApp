package com.stedi.randomimagegenerator.app.di.components

import com.stedi.randomimagegenerator.app.di.modules.ActivityModule
import com.stedi.randomimagegenerator.app.di.modules.GenerationModule
import com.stedi.randomimagegenerator.app.di.modules.HomeModule
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivityModel
import com.stedi.randomimagegenerator.app.view.dialogs.*
import dagger.Subcomponent

@Subcomponent(modules = [(ActivityModule::class)])
interface ActivityComponent {
    fun inject(activityModel: BaseActivityModel)

    fun inject(dialog: ConfirmDialog)

    fun inject(dialog: EditPresetNameDialog)

    fun inject(dialog: SimpleIntegerParamsDialog)

    fun inject(dialog: ColoredNoiseParamsDialog)

    fun inject(dialog: GenerationDialog)

    fun plus(module: HomeModule): HomeComponent

    fun plus(module: GenerationModule): GenerationComponent
}