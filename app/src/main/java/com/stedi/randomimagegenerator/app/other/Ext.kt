package com.stedi.randomimagegenerator.app.other

import android.content.Context
import android.util.SparseArray
import com.stedi.randomimagegenerator.app.App
import java.util.*

fun Context.getApp() = applicationContext as App

fun sleep(millis: Long) {
    try {
        Thread.sleep(millis)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}

fun <T> SparseArray<T>.toList(): List<T> {
    val result = ArrayList<T>()
    for (i in 0 until size()) {
        result.add(valueAt(i))
    }
    return result
}