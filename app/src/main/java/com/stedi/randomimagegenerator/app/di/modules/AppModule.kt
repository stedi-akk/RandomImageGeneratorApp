package com.stedi.randomimagegenerator.app.di.modules

import android.content.Context
import android.os.Environment
import com.stedi.randomimagegenerator.app.App
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.di.*
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.other.Utils
import com.stedi.randomimagegenerator.app.other.logger.Logger
import com.stedi.randomimagegenerator.app.view.components.GeneratorTypeImageLoader
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
    fun provideBus(logger: Logger): CachedBus = CachedBus(logger)

    @Provides
    @DefaultScheduler
    fun provideDefaultScheduler(): Scheduler = Schedulers.io()

    @Provides
    @RigScheduler
    fun provideRigScheduler(): Scheduler = Schedulers.computation()

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

    @Provides
    @Singleton
    fun provideGeneratorTypeImageLoader(@AppContext context: Context, @RigScheduler scheduler: Scheduler, logger: Logger): GeneratorTypeImageLoader {
        return GeneratorTypeImageLoader(Utils.dp2pxi(context, R.dimen.adapter_rig_image_size), scheduler, logger)
    }
}