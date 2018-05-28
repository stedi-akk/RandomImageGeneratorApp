package com.stedi.randomimagegenerator.app.other

import android.graphics.Bitmap
import com.stedi.randomimagegenerator.generators.Generator
import java.io.OutputStream

fun proxy(generator: Generator) = generator

fun Bitmap.compressProxy(format: Bitmap.CompressFormat, quality: Int, stream: OutputStream) = compress(format, quality, stream)