package com.stedi.randomimagegenerator.app.view.components

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.edmodo.rangebar.RangeBar
import com.stedi.randomimagegenerator.app.R

class HSVRangeBar : RangeBar {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val HSV = FloatArray(3)

    private var barBitmap: Bitmap? = null
    private var barWidth = 0
    private var barHeight = 0

    private val barLRPadding by lazy {
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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (barWidth != w || barHeight != h) {
            barWidth = w
            barHeight = h
            post { invalidateBarBitmap() }
        }
    }

    override fun onDraw(canvas: Canvas) {
        val barBitmap = barBitmap
        if (barBitmap != null) {
            canvas.drawBitmap(barBitmap, barLRPadding, 0f, null)
        }
        super.onDraw(canvas)
    }

    private fun invalidateBarBitmap() {
        val barWidth = barWidth - barLRPadding * 2f
        val barHeight = barHeight.toFloat()

        barBitmap = Bitmap.createBitmap(barWidth.toInt(), barHeight.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(barBitmap)

        val hueStep = Math.max(barWidth / 361f, 1f)
        var left = 0f
        for (hue in 0..360) {
            HSV[0] = hue.toFloat()
            paint.color = Color.HSVToColor(HSV)
            canvas.drawRect(left, 0f, left + hueStep, barHeight, paint)
            left += hueStep
        }

        invalidate()
    }
}