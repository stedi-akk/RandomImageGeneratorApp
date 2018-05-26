package com.stedi.randomimagegenerator.app.di.components

import com.stedi.randomimagegenerator.app.di.modules.ActivityModule
import com.stedi.randomimagegenerator.app.di.modules.GenerationModule
import com.stedi.randomimagegenerator.app.view.activity.HomeActivityModel
import com.stedi.randomimagegenerator.app.view.activity.PreviewActivityModel
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import com.stedi.randomimagegenerator.app.view.dialogs.*
import dagger.Subcomponent

@Subcomponent(modules = [(ActivityModule::class)])
interface ActivityComponent {
    fun inject(activity: BaseActivity)

    fun inject(dialog: ConfirmDialog)

    fun inject(dialog: EditPresetNameDialog)

    fun inject(dialog: SimpleIntegerParamsDialog)

    fun inject(dialog: ColoredNoiseParamsDialog)

    fun inject(dialog: GenerationDialog)

    fun inject(homeModel: HomeActivityModel)

    fun inject(previewModel: PreviewActivityModel)

    fun plus(module: GenerationModule): GenerationComponent
}