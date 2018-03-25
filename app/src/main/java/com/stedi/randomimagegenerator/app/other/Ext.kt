package com.stedi.randomimagegenerator.app.other

import android.content.Context
import com.stedi.randomimagegenerator.app.App

fun Context.getApp() = this.applicationContext as App

fun sleep(millis: Long) {
    try {
        Thread.sleep(millis)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}