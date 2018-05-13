package com.stedi.randomimagegenerator.app.di.modules

import com.stedi.randomimagegenerator.app.presenter.impl.HomePresenterImpl
import com.stedi.randomimagegenerator.app.presenter.interfaces.HomePresenter
import dagger.Binds
import dagger.Module

@Module(includes = [(HomeModule.Declarations::class)])
class HomeModule {
    @Module
    interface Declarations {
        @Binds
        fun provideHomePresenter(presenter: HomePresenterImpl): HomePresenter
    }
}