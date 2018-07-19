package com.stedi.randomimagegenerator.app.view.components

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.edmodo.rangebar.RangeBar
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.other.dp2px

class HSVRangeBar : RangeBar {
    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val ignoredAreasPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var strokeColor = context.resources.getColor(R.color.colorPrimary)
    private var strokeWidth = context.dp2px(4f)

    private val HUE_MAX = 360
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
        ignoredAreasPaint.color = context.resources.getColor(R.color.gray_dark_semi_transparent)
        HSV[1] = 100f
        HSV[2] = 100f
        setTickCount(HUE_MAX + 1)
        setBarColor(Color.TRANSPARENT)
        setConnectingLineColor(Color.TRANSPARENT)
        setThumbColorNormal(Color.TRANSPARENT)
        setThumbColorPressed(Color.TRANSPARENT)
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
        drawBar(canvas)
        drawIgnoredAreas(canvas)
        overrideThumbsDrawing()
        super.onDraw(canvas)
    }

    private fun drawIgnoredAreas(canvas: Canvas) {
        val barWidth = barWidth - barLRPadding * 2f
        val barHeight = barHeight.toFloat()

        val hueStep = Math.max(barWidth / HUE_MAX, 1f)

        val leftEnd = hueStep * leftIndex
        val rightStart = hueStep * rightIndex

        canvas.drawRect(barLRPadding, 0f, leftEnd + barLRPadding, barHeight, ignoredAreasPaint)
        canvas.drawRect(rightStart + barLRPadding, 0f, barWidth + barLRPadding, barHeight, ignoredAreasPaint)
    }

    private fun drawBar(canvas: Canvas) {
        val barBitmap = barBitmap
        if (barBitmap != null) {
            canvas.drawBitmap(barBitmap, barLRPadding, 0f, null)
        }
    }

    private fun overrideThumbsDrawing() {
        leftThumb.paintStroke.color = strokeColor
        rightThumb.paintStroke.color = strokeColor
        leftThumb.paintStroke.strokeWidth = strokeWidth
        rightThumb.paintStroke.strokeWidth = strokeWidth
        HSV[0] = leftIndex.toFloat()
        leftThumb.paintNormal.color = Color.HSVToColor(HSV)
        leftThumb.paintPressed.color = leftThumb.paintNormal.color
        HSV[0] = rightIndex.toFloat()
        rightThumb.paintNormal.color = Color.HSVToColor(HSV)
        rightThumb.paintPressed.color = rightThumb.paintNormal.color
    }

    private fun invalidateBarBitmap() {
        val barWidth = barWidth - barLRPadding * 2f
        val barHeight = barHeight.toFloat()

        barBitmap = Bitmap.createBitmap(barWidth.toInt(), barHeight.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(barBitmap)

        val hueStep = Math.max(barWidth / HUE_MAX, 1f)

        var left = 0f
        for (hue in 0..HUE_MAX) {
            HSV[0] = hue.toFloat()
            barPaint.color = Color.HSVToColor(HSV)
            canvas.drawRect(left, 0f, left + hueStep, barHeight, barPaint)
            left += hueStep
        }

        invalidate()
    }
}