package com.stedi.randomimagegenerator.app;

import android.app.Application;
import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.Rig;
import com.stedi.randomimagegenerator.app.di.components.AppComponent;
import com.stedi.randomimagegenerator.app.di.components.DaggerAppComponent;
import com.stedi.randomimagegenerator.app.di.modules.AppModule;

public final class App extends Application {
    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        Rig.enableDebugLogging(!BuildConfig.BUILD_TYPE.equals("release"));
    }

    @NonNull
    public AppComponent getAppComponent() {
        return component;
    }
}
