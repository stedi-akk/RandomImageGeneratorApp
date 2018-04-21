package com.stedi.randomimagegenerator.app.di.modules

import android.content.Context
import com.stedi.randomimagegenerator.app.di.ActivityContext
import com.stedi.randomimagegenerator.app.presenter.impl.ColoredNoiseParamsPresenterImpl
import com.stedi.randomimagegenerator.app.presenter.impl.GenerationPresenterImpl
import com.stedi.randomimagegenerator.app.presenter.impl.SimpleIntegerParamsPresenterImpl
import com.stedi.randomimagegenerator.app.presenter.interfaces.ColoredNoiseParamsPresenter
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationPresenter
import com.stedi.randomimagegenerator.app.presenter.interfaces.SimpleIntegerParamsPresenter
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
    }

    @Provides
    @ActivityContext
    fun provideActivityContext(): Context = activity;
}