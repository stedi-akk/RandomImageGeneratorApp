package com.stedi.randomimagegenerator.app.di.modules;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.App;
import com.stedi.randomimagegenerator.app.other.CachedBus;

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
    @Named("DefaultScheduler")
    Scheduler provideDefaultScheduler() {
        return Schedulers.io();
    }

    @Provides
    @Named("RigScheduler")
    Scheduler provideRigScheduler() {
        return Schedulers.computation();
    }

    @Provides
    @Named("UiScheduler")
    Scheduler provideAndroidScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Named("DefaultSavePath")
    String provideDefaultSavePath() {
        return Environment.getExternalStorageDirectory().getPath();
    }
}
