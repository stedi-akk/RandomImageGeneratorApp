package com.stedi.randomimagegenerator.app.other

import timber.log.Timber

class TimberDebug(private val rootTag: String) : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, rootTag, tag?.let { "$it: $message" } ?: message, t)
    }
}