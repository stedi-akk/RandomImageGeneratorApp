package com.stedi.randomimagegenerator.app.di.modules

import android.content.Context
import com.stedi.randomimagegenerator.app.App
import com.stedi.randomimagegenerator.app.di.AppContext
import com.stedi.randomimagegenerator.app.di.DefaultScheduler
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.other.logger.Logger
import com.stedi.randomimagegenerator.app.presenter.impl.GenerationPresenterImpl
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationPresenter
import dagger.Binds
import dagger.Module
import dagger.Provides
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Singleton

@Module(includes = [(AppModule.Declarations::class)])
class AppModule(private val app: App) {
    @Module
    interface Declarations {
        @Binds
        fun provideGenerationPresenter(presenter: GenerationPresenterImpl): GenerationPresenter
    }

    @Provides
    @Singleton
    @AppContext
    fun provideAppContext(): Context = app

    @Provides
    @Singleton
    fun provideBus(logger: Logger): CachedBus = CachedBus(logger = logger)

    @Provides
    @DefaultScheduler
    fun provideDefaultScheduler(): Scheduler = Schedulers.io()

    @Provides
    @UiScheduler
    fun provideAndroidScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    @Singleton
    fun providePendingPreset(logger: Logger) = PendingPreset(logger)
}