package com.stedi.randomimagegenerator.app.di.modules

import android.content.Context
import com.stedi.randomimagegenerator.app.di.ActivityContext
import com.stedi.randomimagegenerator.app.presenter.impl.GenerationPresenterImpl
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationPresenter
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [(ActivityModule.Declarations::class)])
class ActivityModule(private val activity: BaseActivity) {
    @Module
    interface Declarations {
        @Binds
        fun provideGenerationPresenter(presenter: GenerationPresenterImpl): GenerationPresenter
    }

    @Provides
    @ActivityContext
    fun provideActivityContext(): Context = activity;
}