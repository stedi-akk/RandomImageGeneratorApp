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
import com.stedi.randomimagegenerator.RigPalette
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.model.data.generatorparams.RandomParams
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams
import com.stedi.randomimagegenerator.app.other.compressProxy
import com.stedi.randomimagegenerator.app.presenter.impl.generateBitmap
import com.stedi.randomimagegenerator.generators.Generator
import okio.Okio
import okio.Source
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
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
        private val presetRequestMap = ConcurrentHashMap<Uri, Preset>()

        fun makeFromPreset(target: Preset, width: Int, height: Int, uniqueUri: Boolean, useFourGenerator: Boolean): Uri {
            val preset = target.makeCopy()
            val pathSegment = if (uniqueUri) "${target.hashCode()}_${System.currentTimeMillis()}" else "${target.hashCode()}"
            val uri = Uri.parse("rig:/preset/$pathSegment?width=$width&height=$height&useFourGenerator=$useFourGenerator")
            presetRequestMap.put(uri, preset)
            return uri
        }

        fun makeFromGenerator(mainType: GeneratorType, secondType: GeneratorType?, width: Int, height: Int): Uri {
            return Uri.parse("rig:/generator/$mainType/$secondType?width=$width&height=$height")
        }
    }

    override fun canHandleRequest(data: Request): Boolean {
        return data.uri.scheme == "rig" && !data.uri.pathSegments.isEmpty() && data.uri.queryParameterNames.size >= 2
    }

    override fun load(request: Request, networkPolicy: Int): Result {
        return when (getRequestType(request.uri)) {
            "generator" -> RequestHandler.Result(generateFromGenerator(request.uri), Picasso.LoadedFrom.MEMORY)
            "preset" -> RequestHandler.Result(generateFromPreset(request.uri), Picasso.LoadedFrom.MEMORY)
            else -> throw IllegalStateException("unknown request type")
        }
    }

    private fun getRequestType(uri: Uri): String {
        return uri.pathSegments[0]
    }

    private fun generateFromGenerator(uri: Uri): Bitmap {
        val generatorParams = createGeneratorParams(uri)
        val size = getSize(uri)
        val bitmap = generateBitmap(generatorParams, size[0], size[1])
        if (bitmap == null) {
            throw IOException("failed to generate bitmap from generator")
        }
        return bitmap
    }

    private fun generateFromPreset(uri: Uri): Source {
        val preset: Preset? = presetRequestMap.get(uri)
        if (preset == null) {
            throw IllegalStateException("request map does not have required preset")
        }
        presetRequestMap.remove(uri)
        val size = getSize(uri)
        val bitmap = generatePresetBitmap(preset, size[0], size[1], useFourGenerator(uri))
        if (bitmap == null) {
            throw IOException("failed to generate preset bitmap")
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

    private fun useFourGenerator(uri: Uri): Boolean = uri.getQueryParameter("useFourGenerator") == "true"

    private fun generateBitmap(generatorParams: GeneratorParams, width: Int, height: Int): Bitmap? {
        val generator = wrapRandomParams(generatorParams)
        return generateBitmap(generator, width, height, Quality.png(), RigPalette.allColors())
    }

    private fun generatePresetBitmap(preset: Preset, width: Int, height: Int, useFourGenerator: Boolean): Bitmap? {
        val generator = if (useFourGenerator) {
            wrapRandomParams(preset.getGeneratorParams())
        } else {
            preset.getGeneratorParams().getGenerator()
        }
        return generateBitmap(generator, width, height, preset.getQuality(), preset.getColorsAsPalette())
    }

    // show FourGenerator if RandomParams is requested
    private fun wrapRandomParams(generatorParams: GeneratorParams): Generator {
        var generator = generatorParams.getGenerator()
        val targetParams = (generatorParams as? EffectGeneratorParams)?.target ?: generatorParams
        if (targetParams is RandomParams) {
            generator = FourGenerator(generator)
        }
        return generator
    }

    private fun toInputStream(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int): InputStream {
        val bos = ByteArrayOutputStream()
        bitmap.compressProxy(format, quality, bos)
        return ByteArrayInputStream(bos.toByteArray())
    }
}