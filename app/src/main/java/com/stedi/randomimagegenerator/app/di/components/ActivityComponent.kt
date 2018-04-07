package com.stedi.randomimagegenerator.app.di.components

import com.stedi.randomimagegenerator.app.di.modules.ActivityModule
import com.stedi.randomimagegenerator.app.di.modules.GenerationModule
import com.stedi.randomimagegenerator.app.di.modules.HomeModule
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import dagger.Subcomponent

@Subcomponent(modules = [(ActivityModule::class)])
interface ActivityComponent {
    fun inject(activity: BaseActivity)

    fun plus(module: HomeModule): HomeComponent

    fun plus(module: GenerationModule): GenerationComponent
}