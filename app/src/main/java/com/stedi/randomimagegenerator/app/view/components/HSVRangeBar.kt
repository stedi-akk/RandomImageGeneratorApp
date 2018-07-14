package com.stedi.randomimagegenerator.app.view.components

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.edmodo.rangebar.RangeBar
import com.stedi.randomimagegenerator.app.R

class HSVRangeBar : RangeBar {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val HSV = FloatArray(3)

    private val rangeBarLRPadding by lazy {
        // RangeBar uses this drawable to set left and right paddings
        val bitmap = BitmapFactory.decodeResource(resources, com.edmodo.rangebar.R.drawable.seek_thumb_normal)
        bitmap.width / 2f
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    init {
        HSV[1] = 100f
        HSV[2] = 100f
        setTickCount(360)
        setBarColor(Color.TRANSPARENT)
        setConnectingLineColor(Color.TRANSPARENT)
        setThumbColorNormal(resources.getColor(R.color.colorAccent))
        setThumbColorPressed(resources.getColor(R.color.colorAccent))
    }

    override fun onDraw(canvas: Canvas) {
        val barWidth = width - rangeBarLRPadding * 2f
        val barHeight = height.toFloat()
        val hueStep = Math.max(barWidth / 361f, 1f)

        var left = 0f
        for (hue in 0..360) {
            HSV[0] = hue.toFloat()
            paint.color = Color.HSVToColor(HSV)

            val xStart = left + rangeBarLRPadding
            val xEnd = xStart + hueStep
            canvas.drawRect(xStart, 0f, xEnd, barHeight, paint)

            left += hueStep
        }

        super.onDraw(canvas)
    }
}