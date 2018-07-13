package com.stedi.randomimagegenerator.app.view.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.edmodo.rangebar.RangeBar

class HSVRangeBar : RangeBar {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val HSV = FloatArray(3)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    init {
        setBarColor(Color.TRANSPARENT)
        setConnectingLineColor(Color.TRANSPARENT)
        setTickCount(360)
    }

    override fun onDraw(canvas: Canvas) {
        val width = width
        val height = height

        HSV[1] = 100f
        HSV[2] = 100f

        var hueStep = width / 360f
        if (hueStep < 1f) {
            hueStep = 1f
        }

        var left = 0f

        for (hue in 0..360) {
            HSV[0] = hue.toFloat()
            paint.color = Color.HSVToColor(HSV)
            canvas.drawRect(left, 0f, left + hueStep, height.toFloat(), paint)
            left += hueStep
        }

        super.onDraw(canvas)
    }
}