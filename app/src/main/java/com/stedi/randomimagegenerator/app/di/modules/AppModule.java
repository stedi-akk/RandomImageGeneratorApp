package com.stedi.randomimagegenerator.app.di.modules;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.App;
import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.qualifiers.AppContext;
import com.stedi.randomimagegenerator.app.di.qualifiers.DefaultScheduler;
import com.stedi.randomimagegenerator.app.di.qualifiers.RigScheduler;
import com.stedi.randomimagegenerator.app.di.qualifiers.RootSavePath;
import com.stedi.randomimagegenerator.app.di.qualifiers.UiScheduler;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.Utils;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.view.components.GeneratorTypeImageLoader;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module
public class AppModule {
    private final App app;

    public AppModule(@NonNull App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    @AppContext
    Context provideAppContext() {
        return app;
    }

    @Provides
    @Singleton
    CachedBus provideBus(Logger logger) {
        return new CachedBus(logger);
    }

    @Provides
    @DefaultScheduler
    Scheduler provideDefaultScheduler() {
        return Schedulers.io();
    }

    @Provides
    @RigScheduler
    Scheduler provideRigScheduler() {
        return Schedulers.computation();
    }

    @Provides
    @UiScheduler
    Scheduler provideAndroidScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Singleton
    @RootSavePath
    String provideRootSavePath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + File.separator + "RIG";
    }

    @Provides
    @Singleton
    PendingPreset providePendingPreset(Logger logger, @RootSavePath String rootSavePath) {
        return new PendingPreset(app.getResources().getString(R.string.unsaved_preset_name), rootSavePath, logger);
    }

    @Provides
    @Singleton
    GeneratorTypeImageLoader provideGeneratorTypeImageLoader(@AppContext Context context, @RigScheduler Scheduler scheduler, Logger logger) {
        return new GeneratorTypeImageLoader(Utils.dp2pxi(context, R.dimen.adapter_rig_image_size), scheduler, logger);
    }
}
