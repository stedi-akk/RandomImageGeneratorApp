package com.stedi.randomimagegenerator.app.other.logger

import android.util.Log

class LogCatLogger(private val tag: String) : Logger {

    override fun log(from: Any, message: String?) {
        Log.d(tag, from.javaClass.simpleName + ": " + message)
    }

    override fun log(from: Any, t: Throwable?) {
        Log.d(tag, from.javaClass.simpleName + ": ", t)
    }

    override fun log(from: Any, message: String?, t: Throwable?) {
        Log.d(tag, from.javaClass.simpleName + ": " + message, t)
    }
}