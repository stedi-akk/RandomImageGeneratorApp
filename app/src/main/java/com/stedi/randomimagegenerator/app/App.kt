package com.stedi.randomimagegenerator.app

import android.app.Application
import android.os.StrictMode
import android.support.v7.app.AppCompatDelegate
import com.crashlytics.android.Crashlytics
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import com.squareup.picasso.Picasso
import com.stedi.randomimagegenerator.Rig
import com.stedi.randomimagegenerator.app.di.components.AppComponent
import com.stedi.randomimagegenerator.app.di.components.DaggerAppComponent
import com.stedi.randomimagegenerator.app.di.modules.AppModule
import com.stedi.randomimagegenerator.app.view.components.RigRequestHandler
import io.fabric.sdk.android.Fabric

class App : Application() {

    lateinit var leakWatcher: RefWatcher
        private set
    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        val debug = BuildConfig.BUILD_TYPE != "release"

        if (debug) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
        }

        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        leakWatcher = LeakCanary.install(this)

        if (!debug) {
            Fabric.with(this, Crashlytics())
        }

        Picasso.setSingletonInstance(Picasso.Builder(this).loggingEnabled(debug)
                .addRequestHandler(RigRequestHandler()).build())

        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        Rig.enableDebugLogging(debug)
    }
}