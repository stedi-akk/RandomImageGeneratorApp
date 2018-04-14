package com.stedi.randomimagegenerator.app.view.components

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.SparseArray
import com.stedi.randomimagegenerator.ImageParams
import com.stedi.randomimagegenerator.Rig
import com.stedi.randomimagegenerator.app.di.DefaultScheduler
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams
import com.stedi.randomimagegenerator.app.other.logger.Logger
import com.stedi.randomimagegenerator.callbacks.GenerateCallback
import rx.Completable
import rx.Scheduler
import java.lang.Exception
import java.util.*

class GeneratorTypeImageLoader(
        private val imageSize: Int,
        @DefaultScheduler private val subscribeOn: Scheduler,
        private val logger: Logger) {

    private val cache = SparseArray<CacheItem>()
    private val callbacks = SparseArray<MutableList<Callback>>()

    private val uiHandler = Handler(Looper.getMainLooper())

    interface Callback {
        fun onLoaded(params: GeneratorParams, bitmap: Bitmap)
    }

    class CacheItem(val params: GeneratorParams, val bitmap: Bitmap)

    fun load(mainType: GeneratorType, secondType: GeneratorType?, callback: Callback) {
        val key = createCacheKey(mainType, secondType)

        cache.get(key)?.apply {
            callback.onLoaded(params, bitmap)
            return
        }

        if (callbacks.indexOfKey(key) >= 0) {
            callbacks.get(key).add(callback)
        } else {
            val list = ArrayList<Callback>()
            list.add(callback)
            callbacks.put(key, list)
        }

        if (cache.indexOfKey(key) >= 0) {
            return
        }
        cache.put(key, null)

        Completable.fromAction {
            val params = createGeneratorParams(mainType, secondType)
            Rig.Builder()
                    .setGenerator(params.getGenerator())
                    .setCount(1)
                    .setFixedSize(imageSize, imageSize)
                    .setCallback(object : GenerateCallback {
                        override fun onGenerated(imageParams: ImageParams, bitmap: Bitmap) {
                            uiHandler.post {
                                cache.put(key, CacheItem(params, bitmap))
                                for (cb in callbacks.get(key)) {
                                    cb.onLoaded(params, bitmap)
                                }
                                callbacks.remove(key)
                            }
                        }

                        override fun onFailedToGenerate(imageParams: ImageParams?, e: Exception?) {
                            logger.log(this@GeneratorTypeImageLoader, "onFailedToGenerate", e)
                        }
                    }).build().generate()
        }.subscribeOn(subscribeOn).subscribe()
    }

    private fun createCacheKey(mainType: GeneratorType, secondType: GeneratorType?): Int {
        if (secondType == null) {
            if (mainType.isEffect) {
                throw IllegalStateException("incorrect behavior")
            }
            return mainType.ordinal
        } else {
            if (!mainType.isEffect || secondType.isEffect) {
                throw IllegalStateException("incorrect behavior")
            }
            return Integer.parseInt(mainType.ordinal.toString() + "" + secondType.ordinal)
        }
    }

    private fun createGeneratorParams(mainType: GeneratorType, secondType: GeneratorType?): GeneratorParams {
        return if (secondType == null) {
            GeneratorParams.createDefaultParams(mainType)
        } else {
            GeneratorParams.createDefaultEffectParams(mainType, GeneratorParams.createDefaultParams(secondType))
        }
    }
}