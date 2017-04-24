package com.stedi.randomimagegenerator.app.di.modules;

import android.content.Context;

import com.stedi.randomimagegenerator.app.App;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    @Named("AppContext")
    Context provideAppContext() {
        return app;
    }
}
