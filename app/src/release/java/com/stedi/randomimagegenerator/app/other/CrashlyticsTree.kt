package com.stedi.randomimagegenerator.app.other

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber

class CrashlyticsTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority != Log.ERROR) {
            return
        }

        if (t != null) {
            Crashlytics.logException(t)
        }
    }
}