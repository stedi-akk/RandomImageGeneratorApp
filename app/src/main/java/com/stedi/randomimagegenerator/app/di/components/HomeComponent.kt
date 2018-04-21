package com.stedi.randomimagegenerator.app.di.components

import com.stedi.randomimagegenerator.app.di.modules.HomeModule
import com.stedi.randomimagegenerator.app.view.activity.HomeActivityModel
import dagger.Subcomponent

@Subcomponent(modules = [(HomeModule::class)])
interface HomeComponent {
    fun inject(homeModel: HomeActivityModel)
}