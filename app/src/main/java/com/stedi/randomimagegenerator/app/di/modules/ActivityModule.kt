package com.stedi.randomimagegenerator.app.di.modules

import android.content.Context
import com.stedi.randomimagegenerator.app.di.ActivityContext
import com.stedi.randomimagegenerator.app.presenter.impl.*
import com.stedi.randomimagegenerator.app.presenter.interfaces.*
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [(ActivityModule.Declarations::class)])
class ActivityModule(private val activity: BaseActivity) {
    @Module
    interface Declarations {
        @Binds
        fun provideEditColoredCirclesPresenter(presenter: SimpleIntegerParamsPresenterImpl): SimpleIntegerParamsPresenter

        @Binds
        fun provideColoredNoiseParamsPresenter(presenter: ColoredNoiseParamsPresenterImpl): ColoredNoiseParamsPresenter

        @Binds
        fun provideGenerationPresenter(presenter: GenerationPresenterImpl): GenerationPresenter

        @Binds
        fun providePreviewGenerationPresenter(presenter: PreviewGenerationPresenterImpl): PreviewGenerationPresenter

        @Binds
        fun provideHomePresenter(presenter: HomePresenterImpl): HomePresenter
    }

    @Provides
    @ActivityContext
    fun provideActivityContext(): Context = activity
}