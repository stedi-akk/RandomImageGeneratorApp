package com.stedi.randomimagegenerator.app.di.components

import com.stedi.randomimagegenerator.app.di.modules.ActivityModule
import com.stedi.randomimagegenerator.app.di.modules.AppModule
import com.stedi.randomimagegenerator.app.di.modules.BuildTypeDependentModule
import com.stedi.randomimagegenerator.app.view.dialogs.ConfirmDialog
import com.stedi.randomimagegenerator.app.view.dialogs.EditPresetNameDialog
import dagger.Component
import javax.inject.Singleton

@Component(modules = [(AppModule::class), (BuildTypeDependentModule::class)])
@Singleton
interface AppComponent {
    fun plus(module: ActivityModule): ActivityComponent

    fun inject(dialog: ConfirmDialog)

    fun inject(dialog: EditPresetNameDialog)
}