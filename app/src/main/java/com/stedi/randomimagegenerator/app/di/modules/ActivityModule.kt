package com.stedi.randomimagegenerator.app.di.modules

import android.content.Context
import com.stedi.randomimagegenerator.app.di.ActivityContext
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: BaseActivity) {

    @Provides
    @ActivityContext
    fun provideActivityContext(): Context = activity;
}