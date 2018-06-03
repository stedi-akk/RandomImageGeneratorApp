package com.stedi.randomimagegenerator.app.other

import android.graphics.Bitmap
import com.stedi.randomimagegenerator.app.BITMAP_COMPRESS_SLEEP
import com.stedi.randomimagegenerator.app.model.data.SlowGenerator
import com.stedi.randomimagegenerator.generators.Generator
import java.io.OutputStream

fun proxy(generator: Generator): Generator = SlowGenerator(generator)

fun Bitmap.compressProxy(format: Bitmap.CompressFormat, quality: Int, stream: OutputStream): Boolean {
    sleep(BITMAP_COMPRESS_SLEEP)
    return compress(format, quality, stream)
}