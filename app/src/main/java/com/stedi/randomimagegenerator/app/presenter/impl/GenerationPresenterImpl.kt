package com.stedi.randomimagegenerator.app.presenter.impl

import android.graphics.Bitmap
import com.squareup.otto.Subscribe
import com.stedi.randomimagegenerator.DefaultFileNamePolicy
import com.stedi.randomimagegenerator.ImageParams
import com.stedi.randomimagegenerator.Quality
import com.stedi.randomimagegenerator.Rig
import com.stedi.randomimagegenerator.app.di.DefaultScheduler
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.other.LockedBus
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationPresenter
import com.stedi.randomimagegenerator.callbacks.GenerateCallback
import com.stedi.randomimagegenerator.callbacks.SaveCallback
import com.stedi.randomimagegenerator.generators.Generator
import rx.Emitter
import rx.Observable
import rx.Scheduler
import rx.Subscription
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class GenerationPresenterImpl @Inject constructor(
        @DefaultScheduler private val subscribeOn: Scheduler,
        @UiScheduler private val observeOn: Scheduler,
        private val bus: LockedBus) : GenerationPresenter {

    private var ui: GenerationPresenter.UIImpl? = null

    @Volatile private var subscription: Subscription? = null
    @Volatile private var rig: Rig? = null

    private var generatedCount: Int = 0
    private var failedCount: Int = 0

    private var generatedCountPosted: Int = 0
    private var failedCountPosted: Int = 0

    class GenerationResult(val generatedCount: Int, val failedCount: Int)
    class GenerationEnd(val throwable: Throwable? = null)

    init {
        bus.register(this)
    }

    override fun onAttach(ui: GenerationPresenter.UIImpl) {
        this.ui = ui
    }

    override fun onDetach() {
        ui = null
    }

    override fun onDestroy() {
        bus.unregister(this)
    }

    override fun startGeneration(preset: Preset) {
        if (subscription != null || rig != null) {
            return
        }

        generatedCount = 0
        failedCount = 0

        generatedCountPosted = -1
        failedCountPosted = -1

        subscription = Observable.create<GenerationResult>({ subscriber ->
            try {
                rig = Rig.Builder().apply {
                    setGenerator(preset.getGeneratorParams().getGenerator())

                    if (preset.getWidth() != 0) {
                        setFixedWidth(preset.getWidth())
                    }
                    if (preset.getHeight() != 0) {
                        setFixedHeight(preset.getHeight())
                    }
                    if (preset.getCount() != 0) {
                        setCount(preset.getCount())
                    }

                    preset.getWidthRange()?.apply {
                        setWidthRange(this[0], this[1], this[2])
                    }

                    preset.getHeightRange()?.apply {
                        setHeightRange(this[0], this[1], this[2])
                    }

                    setQuality(preset.getQuality())
                    setFileNamePolicy(DefaultFileNamePolicy())
                    setFileSavePath(preset.pathToSave)

                    setCallback(object : GenerateCallback {
                        override fun onGenerated(imageParams: ImageParams, bitmap: Bitmap) {
                            if (!isCanceled()) {
                                ui?.imageGenerated(imageParams, bitmap)
                            }
                        }

                        override fun onFailedToGenerate(imageParams: ImageParams, e: Exception) {
                            if (!isCanceled()) {
                                failedCount++
                                Timber.e(e)
                                subscriber.onNext(GenerationResult(generatedCount, failedCount))
                            }
                        }
                    })

                    setFileSaveCallback(object : SaveCallback {
                        override fun onSaved(bitmap: Bitmap, file: File) {
                            if (!isCanceled()) {
                                generatedCount++
                                ui?.imageSaved(bitmap, file)
                                subscriber.onNext(GenerationResult(generatedCount, failedCount))
                            }
                        }

                        override fun onFailedToSave(bitmap: Bitmap, e: Exception) {
                            if (!isCanceled()) {
                                failedCount++
                                Timber.e(e)
                                subscriber.onNext(GenerationResult(generatedCount, failedCount))
                            }
                        }
                    })
                }.build()
                rig?.generate()

                subscriber.onCompleted()
            } catch (t: Throwable) {
                subscriber.onError(t)
            }

            subscription = null
            rig = null
            Timber.d("startGeneration finished")

        }, Emitter.BackpressureMode.LATEST)
                .subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .skipWhile { bus.isLocked }
                .doOnTerminate {
                    if (generatedCountPosted != generatedCount || failedCountPosted != failedCount) {
                        bus.post(GenerationResult(generatedCount, failedCount))
                    }
                }.subscribe({ result ->
                    generatedCountPosted = result.generatedCount
                    failedCountPosted = result.failedCount
                    bus.post(result)
                }, { throwable ->
                    bus.post(GenerationEnd(throwable))
                }, {
                    bus.post(GenerationEnd())
                })
    }

    override fun cancelGeneration() {
        Timber.d("cancelGeneration")
        subscription?.unsubscribe()
    }

    @Subscribe
    fun onGenerationResult(result: GenerationResult) {
        ui?.onResult(result.generatedCount, result.failedCount) ?: let { cancelGeneration() }
    }

    @Subscribe
    fun onGenerationEnd(result: GenerationEnd) {
        Timber.d("onGenerationEnd")
        cancelGeneration()
        result.throwable?.apply {
            Timber.e(this)
            ui?.onGenerationFailed()
        } ?: ui?.onFinishGeneration()
    }

    private fun isCanceled(): Boolean {
        if (subscription?.isUnsubscribed == true) {
            rig?.cancel()
            return true
        }
        return false
    }
}

// simple one-shot generation
fun generateBitmap(generator: Generator, width: Int, height: Int, quality: Quality): Bitmap? {
    var result: Bitmap? = null

    Rig.Builder().setGenerator(generator)
            .setCount(1).setFixedSize(width, height).setQuality(quality)
            .setCallback(object : GenerateCallback {
                override fun onGenerated(imageParams: ImageParams, bitmap: Bitmap) {
                    result = bitmap
                }

                override fun onFailedToGenerate(imageParams: ImageParams, e: java.lang.Exception) {
                    Timber.e(e)
                }
            }).build().generate()

    return result
}