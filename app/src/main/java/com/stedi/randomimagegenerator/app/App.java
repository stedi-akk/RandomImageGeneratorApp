package com.stedi.randomimagegenerator.app;

import android.app.Application;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.stedi.randomimagegenerator.Rig;
import com.stedi.randomimagegenerator.app.di.components.AppComponent;
import com.stedi.randomimagegenerator.app.di.components.DaggerAppComponent;
import com.stedi.randomimagegenerator.app.di.modules.AppModule;

public final class App extends Application {
    private static App instance;

    private RefWatcher leakWatcher;
    private AppComponent component;

    @Override
    public void onCreate() {
        instance = this;

        boolean debug = !BuildConfig.BUILD_TYPE.equals("release");

        if (debug) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }

        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        leakWatcher = LeakCanary.install(this);

        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Rig.enableDebugLogging(debug);
    }

    @NonNull
    public static App getInstance() {
        return instance;
    }

    @NonNull
    public AppComponent getAppComponent() {
        return component;
    }

    @NonNull
    public RefWatcher getLeakWatcher() {
        return leakWatcher;
    }
}
