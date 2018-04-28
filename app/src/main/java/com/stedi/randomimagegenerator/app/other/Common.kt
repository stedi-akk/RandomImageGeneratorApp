package com.stedi.randomimagegenerator.app.other

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.annotation.DimenRes
import android.support.annotation.StringRes
import android.util.SparseArray
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import android.widget.NumberPicker
import android.widget.Toast
import com.stedi.randomimagegenerator.app.App
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

// https://stackoverflow.com/questions/24233556/changing-numberpicker-divider-color
fun NumberPicker.setDividerColor(color: Int) {
    for (pf in NumberPicker::class.java.declaredFields) {
        if (pf.name == "mSelectionDivider") {
            pf.isAccessible = true
            try {
                pf.set(this, ColorDrawable(color))
            } catch (e: Exception) {
                // ignore
            }
            break
        }
    }
}

fun formatTime(millis: Long): String = LazyCommon.dateFormat.format(Date(millis))

@SuppressLint("SimpleDateFormat")
private object LazyCommon {
    val dateFormat: SimpleDateFormat by lazy {
        SimpleDateFormat("dd.MM.yyyy HH:mm")
    }
}