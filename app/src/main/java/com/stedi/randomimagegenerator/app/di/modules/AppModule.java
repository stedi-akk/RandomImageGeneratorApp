package com.stedi.randomimagegenerator.app.di.modules;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.App;
import com.stedi.randomimagegenerator.app.di.qualifiers.DefaultScheduler;
import com.stedi.randomimagegenerator.app.di.qualifiers.RigScheduler;
import com.stedi.randomimagegenerator.app.di.qualifiers.UiScheduler;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.logger.Logger;

import javax.inject.Named;
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
    @Named("AppContext")
    Context provideAppContext() {
        return app;
    }

    @Provides
    @Singleton
    CachedBus provideBus() {
        return new CachedBus();
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
    PendingPreset providePendingPreset(Logger logger) {
        return new PendingPreset("Unsaved preset", Environment.getExternalStorageDirectory().getPath(), logger);
    }
}
