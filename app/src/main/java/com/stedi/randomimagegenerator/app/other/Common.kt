package com.stedi.randomimagegenerator.app.other

import android.app.Activity
import android.content.Context
import android.support.annotation.DimenRes
import android.support.annotation.StringRes
import android.util.SparseArray
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.stedi.randomimagegenerator.app.App
import com.stedi.randomimagegenerator.app.di.RootSavePath
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun Context.getApp() = applicationContext as App

fun Context.dim2px(@DimenRes id: Int): Int {
    return resources.getDimensionPixelOffset(id)
}

fun Context.dp2px(dp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
}

fun Context.showToast(@StringRes id: Int, duration: Int = Toast.LENGTH_LONG) {
    showToast(resources.getString(id), duration)
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

fun Activity.hideInput() {
    currentFocus?.apply {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }
}

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

fun formatTime(millis: Long): String = LazyCommon.dateFormat.format(Date(millis))

fun formatSavePath(@RootSavePath rootSavePath: String, path: String): String {
    return path.replace(rootSavePath + File.separator, "sdcard/Pictures/RIG/")
}

private object LazyCommon {
    val dateFormat: SimpleDateFormat by lazy {
        SimpleDateFormat("dd.MM.yyyy HH:mm")
    }
}