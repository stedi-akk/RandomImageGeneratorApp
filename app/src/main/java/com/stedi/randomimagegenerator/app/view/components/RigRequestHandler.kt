package com.stedi.randomimagegenerator.app.view.components

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import com.squareup.picasso.Picasso
import com.squareup.picasso.Request
import com.squareup.picasso.RequestHandler
import com.stedi.randomimagegenerator.ImageParams
import com.stedi.randomimagegenerator.Quality
import com.stedi.randomimagegenerator.Rig
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.model.data.generatorparams.RandomParams
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams
import com.stedi.randomimagegenerator.callbacks.GenerateCallback
import com.stedi.randomimagegenerator.generators.Generator
import okio.Okio
import timber.log.Timber
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception

// for thumbnail with 4 generated images
class FourGenerator(private val target: Generator) : Generator {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun generate(imageParams: ImageParams): Bitmap? {
        val scaledWidth = Math.ceil((imageParams.width / 2f).toDouble()).toInt()
        val scaledHeight = Math.ceil((imageParams.height / 2f).toDouble()).toInt()

        val parts = arrayOfNulls<Bitmap>(4)
        for (i in 0..3) {
            val targetBitmap = target.generate(imageParams) ?: return null
            parts[i] = Bitmap.createScaledBitmap(targetBitmap, scaledWidth, scaledHeight, true)
            targetBitmap.recycle()
        }

        val resultBitmap = Bitmap.createBitmap(imageParams.width, imageParams.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)

        canvas.drawBitmap(parts[0], 0f, 0f, paint)
        canvas.drawBitmap(parts[1], imageParams.width / 2f, 0f, paint)
        canvas.drawBitmap(parts[2], 0f, imageParams.height / 2f, paint)
        canvas.drawBitmap(parts[3], imageParams.width / 2f, imageParams.height / 2f, paint)

        for (bitmap in parts) {
            bitmap?.recycle()
        }

        return resultBitmap
    }
}

// showing generated images with Picasso
class RigRequestHandler : RequestHandler() {

    companion object {
        fun makeUri(mainType: GeneratorType, secondType: GeneratorType?, width: Int, height: Int, format: CompressFormat = Bitmap.CompressFormat.PNG, quality: Int = 100): Uri {
            return Uri.parse("rig:/$mainType${secondType?.let { "/$it" } ?: ""}?width=$width&height=$height&format=$format&quality=$quality")
        }
    }

    override fun canHandleRequest(data: Request): Boolean {
        return data.uri.scheme == "rig" && !data.uri.pathSegments.isEmpty() && data.uri.queryParameterNames.size == 4
    }

    override fun load(request: Request, networkPolicy: Int): Result {
        val generatorParams = createGeneratorParams(getMainType(request.uri), getSecondType(request.uri))
        val size = getSize(request.uri)
        val format = getCompressFormat(request.uri)
        val quality = getQualityValue(request.uri)

        val bitmap = generate(generatorParams, size[0], size[1], Quality(format, quality))
        if (bitmap == null) {
            throw IOException("failed to generate bitmap")
        }

        if (format == CompressFormat.PNG) {
            return RequestHandler.Result(bitmap, Picasso.LoadedFrom.MEMORY)
        } else {
            val source = Okio.source(toInputStream(bitmap, format, quality))
            return RequestHandler.Result(source, Picasso.LoadedFrom.MEMORY)
        }
    }

    private fun getMainType(uri: Uri): GeneratorType {
        val path = uri.pathSegments[0]
        return GeneratorType.values().first { it.name == path }
    }

    private fun getSecondType(uri: Uri): GeneratorType? {
        if (uri.pathSegments.size >= 2) {
            val path = uri.pathSegments[1]
            return GeneratorType.values().first { it.name == path }
        }
        return null
    }

    private fun getSize(uri: Uri): Array<Int> {
        return arrayOf(uri.getQueryParameter("width").toInt(), uri.getQueryParameter("height").toInt())
    }

    private fun createGeneratorParams(mainType: GeneratorType, secondType: GeneratorType?): GeneratorParams {
        return if (secondType == null) {
            GeneratorParams.createDefaultParams(mainType)
        } else {
            GeneratorParams.createDefaultEffectParams(mainType, GeneratorParams.createDefaultParams(secondType))
        }
    }

    private fun getCompressFormat(uri: Uri): CompressFormat {
        val format = uri.getQueryParameter("format")
        return CompressFormat.values().first { it.name == format }
    }

    private fun getQualityValue(uri: Uri): Int {
        return uri.getQueryParameter("quality").toInt()
    }

    private fun generate(generatorParams: GeneratorParams, width: Int, height: Int, quality: Quality): Bitmap? {
        var result: Bitmap? = null

        // show 4 images at once if RandomParams is requested
        var generator = generatorParams.getGenerator()
        val targetParams = (generatorParams as? EffectGeneratorParams)?.target ?: generatorParams
        if (targetParams is RandomParams) {
            generator = FourGenerator(generator)
        }

        Rig.Builder().setGenerator(generator)
                .setCount(1).setFixedSize(width, height).setQuality(quality)
                .setCallback(object : GenerateCallback {
                    override fun onGenerated(imageParams: ImageParams, bitmap: Bitmap) {
                        result = bitmap
                    }

                    override fun onFailedToGenerate(imageParams: ImageParams, e: Exception) {
                        Timber.e(e)
                    }
                }).build().generate()

        return result
    }

    private fun toInputStream(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int): InputStream {
        val bos = ByteArrayOutputStream()
        bitmap.compress(format, quality, bos)
        return ByteArrayInputStream(bos.toByteArray())
    }
}