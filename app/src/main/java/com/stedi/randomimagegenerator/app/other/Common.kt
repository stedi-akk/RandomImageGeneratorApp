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
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.generators.ColoredNoiseGenerator
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

@StringRes
fun GeneratorType.nameRes(): Int {
    return when (this) {
        GeneratorType.COLORED_CIRCLES -> R.string.generator_colored_circles
        GeneratorType.COLORED_RECTANGLE -> R.string.generator_colored_rectangle
        GeneratorType.COLORED_PIXELS -> R.string.generator_colored_pixels
        GeneratorType.FLAT_COLOR -> R.string.generator_flat_color
        GeneratorType.COLORED_NOISE -> R.string.generator_colored_noise
        GeneratorType.COLORED_LINES -> R.string.generator_colored_lines
        GeneratorType.MIRRORED -> R.string.effect_mirrored
        GeneratorType.THRESHOLD -> R.string.effect_threshold
        GeneratorType.TEXT_OVERLAY -> R.string.effect_text_overlay
    }
}

@StringRes
fun ColoredNoiseGenerator.Orientation.nameRes(): Int {
    return when (this) {
        ColoredNoiseGenerator.Orientation.VERTICAL -> R.string.vertical
        ColoredNoiseGenerator.Orientation.HORIZONTAL -> R.string.horizontal
        ColoredNoiseGenerator.Orientation.RANDOM -> R.string.random
    }
}

@StringRes
fun ColoredNoiseGenerator.Type.nameRes(): Int {
    return when (this) {
        ColoredNoiseGenerator.Type.TYPE_1 -> R.string.type_1
        ColoredNoiseGenerator.Type.TYPE_2 -> R.string.type_2
        ColoredNoiseGenerator.Type.TYPE_3 -> R.string.type_3
        ColoredNoiseGenerator.Type.TYPE_4 -> R.string.type_4
        ColoredNoiseGenerator.Type.TYPE_5 -> R.string.type_5
        ColoredNoiseGenerator.Type.TYPE_6 -> R.string.type_6
        ColoredNoiseGenerator.Type.RANDOM -> R.string.random
    }
}

fun formatTime(millis: Long): String = LazyCommon.dateFormat.format(Date(millis))

@SuppressLint("SimpleDateFormat")
private object LazyCommon {
    val dateFormat: SimpleDateFormat by lazy {
        SimpleDateFormat("dd.MM.yyyy HH:mm")
    }
}