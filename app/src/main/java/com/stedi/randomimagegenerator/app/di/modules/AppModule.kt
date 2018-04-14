package com.stedi.randomimagegenerator.app.di.modules

import android.content.Context
import android.os.Environment
import com.stedi.randomimagegenerator.app.App
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.di.AppContext
import com.stedi.randomimagegenerator.app.di.DefaultScheduler
import com.stedi.randomimagegenerator.app.di.RootSavePath
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.other.logger.Logger
import dagger.Module
import dagger.Provides
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {

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
    @RootSavePath
    fun provideRootSavePath(): String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + File.separator + "RIG"

    @Provides
    @Singleton
    fun providePendingPreset(logger: Logger, @RootSavePath rootSavePath: String): PendingPreset {
        return PendingPreset(app.resources.getString(R.string.unsaved_preset_name), rootSavePath, logger)
    }
}