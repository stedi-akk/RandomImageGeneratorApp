package com.stedi.randomimagegenerator.app.view.components

import android.graphics.Bitmap
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
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.model.data.generatorparams.RandomParams
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams
import com.stedi.randomimagegenerator.callbacks.GenerateCallback
import com.stedi.randomimagegenerator.generators.Generator
import okio.Okio
import okio.Source
import timber.log.Timber
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.util.concurrent.ConcurrentHashMap

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
        private val previewRequestMap = ConcurrentHashMap<Uri, Preset>()

        fun makePreviewUri(preset: Preset, width: Int, height: Int): Uri {
            val previewPreset = preset.makeCopy()
            val uri = Uri.parse("rig:/preview/${previewPreset.hashCode()}?width=$width&height=$height")
            previewRequestMap.put(uri, previewPreset)
            return uri
        }

        fun makeThumbnailUri(mainType: GeneratorType, secondType: GeneratorType?, width: Int, height: Int): Uri {
            return Uri.parse("rig:/thumbnail/$mainType/$secondType?width=$width&height=$height")
        }
    }

    override fun canHandleRequest(data: Request): Boolean {
        return data.uri.scheme == "rig" && !data.uri.pathSegments.isEmpty() && data.uri.queryParameterNames.size == 2
    }

    override fun load(request: Request, networkPolicy: Int): Result {
        return when (getRequestType(request.uri)) {
            "thumbnail" -> RequestHandler.Result(generateThumbnail(request.uri), Picasso.LoadedFrom.MEMORY)
            "preview" -> RequestHandler.Result(generatePreview(request.uri), Picasso.LoadedFrom.MEMORY)
            else -> throw IllegalStateException("unknown request type")
        }
    }

    private fun getRequestType(uri: Uri): String {
        return uri.pathSegments[0]
    }

    private fun generateThumbnail(uri: Uri): Bitmap {
        val generatorParams = createGeneratorParams(uri)
        val size = getSize(uri)
        val bitmap = generateThumbnailBitmap(generatorParams, size[0], size[1])
        if (bitmap == null) {
            throw IOException("failed to generate thumbnail bitmap")
        }
        return bitmap
    }

    private fun generatePreview(uri: Uri): Source {
        val preset: Preset? = previewRequestMap.get(uri)
        if (preset == null) {
            throw IllegalStateException("request map does not have required preset")
        }
        previewRequestMap.remove(uri)
        val size = getSize(uri)
        val bitmap = generatePreviewBitmap(preset, size[0], size[1])
        if (bitmap == null) {
            throw IOException("failed to generate preview bitmap")
        }
        return Okio.source(toInputStream(bitmap, preset.getQuality().format, preset.getQuality().qualityValue))
    }

    private fun createGeneratorParams(uri: Uri): GeneratorParams {
        val mainType = GeneratorType.values().first { it.name == uri.pathSegments[1] }
        val secondType = GeneratorType.values().firstOrNull { it.name == uri.pathSegments[2] }
        return if (secondType == null) {
            GeneratorParams.createDefaultParams(mainType)
        } else {
            GeneratorParams.createDefaultEffectParams(mainType, GeneratorParams.createDefaultParams(secondType))
        }
    }

    private fun getSize(uri: Uri): Array<Int> {
        return arrayOf(uri.getQueryParameter("width").toInt(), uri.getQueryParameter("height").toInt())
    }

    private fun generateThumbnailBitmap(generatorParams: GeneratorParams, width: Int, height: Int): Bitmap? {
        // show 4 images at once if RandomParams is requested
        var generator = generatorParams.getGenerator()
        val targetParams = (generatorParams as? EffectGeneratorParams)?.target ?: generatorParams
        if (targetParams is RandomParams) {
            generator = FourGenerator(generator)
        }
        return generateBitmap(generator, width, height, Quality.png())
    }

    private fun generatePreviewBitmap(preset: Preset, width: Int, height: Int): Bitmap? {
        return generateBitmap(preset.getGeneratorParams().getGenerator(), width, height, preset.getQuality())
    }

    private fun generateBitmap(generator: Generator, width: Int, height: Int, quality: Quality): Bitmap? {
        var result: Bitmap? = null

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