package com.stedi.randomimagegenerator.app.di.components

import com.stedi.randomimagegenerator.app.di.modules.ActivityModule
import com.stedi.randomimagegenerator.app.di.modules.AppModule
import com.stedi.randomimagegenerator.app.di.modules.BuildTypeDependentModule
import com.stedi.randomimagegenerator.app.view.activity.base.LifeCycleActivity
import com.stedi.randomimagegenerator.app.view.dialogs.ConfirmDialog
import com.stedi.randomimagegenerator.app.view.dialogs.EditPresetNameDialog
import com.stedi.randomimagegenerator.app.view.dialogs.GenerationDialog
import com.stedi.randomimagegenerator.app.view.fragments.base.LifeCycleFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [(AppModule::class), (BuildTypeDependentModule::class)])
@Singleton
interface AppComponent {
    fun plus(module: ActivityModule): ActivityComponent

    fun inject(fragment: LifeCycleFragment)

    fun inject(fragment: LifeCycleActivity)

    fun inject(dialog: ConfirmDialog)

    fun inject(dialog: EditPresetNameDialog)

    fun inject(dialog: GenerationDialog)
}