package com.stedi.randomimagegenerator.app.di.modules

import android.content.Context
import android.os.Environment
import com.stedi.randomimagegenerator.app.App
import com.stedi.randomimagegenerator.app.di.AppContext
import com.stedi.randomimagegenerator.app.di.DefaultScheduler
import com.stedi.randomimagegenerator.app.di.SaveFolder
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.other.LockedBus
import dagger.Module
import dagger.Provides
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {

    @Provides
    @Singleton
    @AppContext
    fun provideAppContext(): Context = app

    @Provides
    @Singleton
    fun provideBus(): LockedBus = LockedBus()

    @Provides
    @DefaultScheduler
    fun provideDefaultScheduler(): Scheduler = Schedulers.io()

    @Provides
    @UiScheduler
    fun provideAndroidScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    @Singleton
    fun providePendingPreset() = PendingPreset()

    @Provides
    @Singleton
    @SaveFolder
    fun provideSaveFolder(): String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).resolve("RIG").path
}