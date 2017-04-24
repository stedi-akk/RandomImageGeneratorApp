package com.stedi.randomimagegenerator.app;

import android.app.Application;

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
    }

    public AppComponent getComponent() {
        return component;
    }
}
