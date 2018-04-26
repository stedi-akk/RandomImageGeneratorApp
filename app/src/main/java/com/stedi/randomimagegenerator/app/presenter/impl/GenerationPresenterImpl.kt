package com.stedi.randomimagegenerator.app.presenter.impl

import android.graphics.Bitmap
import com.squareup.otto.Subscribe
import com.stedi.randomimagegenerator.DefaultFileNamePolicy
import com.stedi.randomimagegenerator.ImageParams
import com.stedi.randomimagegenerator.Rig
import com.stedi.randomimagegenerator.app.di.DefaultScheduler
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.other.LockedBus
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationPresenter
import com.stedi.randomimagegenerator.callbacks.GenerateCallback
import com.stedi.randomimagegenerator.callbacks.SaveCallback
import rx.Observable
import rx.Scheduler
import rx.Subscriber
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class GenerationPresenterImpl @Inject constructor(
        @DefaultScheduler private val subscribeOn: Scheduler,
        @UiScheduler private val observeOn: Scheduler,
        private val bus: LockedBus) : GenerationPresenter {

    private var ui: GenerationPresenter.UIImpl? = null
    private var generationInProgress: Boolean = false

    class GenerationEvent(val type: Type, val imageParams: ImageParams? = null, val imageFile: File? = null) {
        enum class Type {
            ON_START_GENERATION,
            ON_GENERATION_UNKNOWN_ERROR,
            ON_FINISH_GENERATION,
            ON_GENERATED,
            ON_FAILED_TO_GENERATE
        }
    }

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
        if (generationInProgress) {
            return
        }
        generationInProgress = true

        Observable.unsafeCreate(object : Observable.OnSubscribe<GenerationEvent> {
            private var generationFor: ImageParams? = null

            override fun call(subscriber: Subscriber<in GenerationEvent>) {
                try {
                    subscriber.onNext(GenerationEvent(GenerationEvent.Type.ON_START_GENERATION))

                    Rig.Builder().apply {
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
                                generationFor = imageParams
                            }

                            override fun onFailedToGenerate(imageParams: ImageParams, e: Exception) {
                                Timber.w(e)
                                subscriber.onNext(GenerationEvent(GenerationEvent.Type.ON_FAILED_TO_GENERATE, imageParams))
                            }
                        })

                        setFileSaveCallback(object : SaveCallback {
                            override fun onSaved(bitmap: Bitmap, file: File) {
                                subscriber.onNext(GenerationEvent(GenerationEvent.Type.ON_GENERATED, generationFor, file))
                            }

                            override fun onFailedToSave(bitmap: Bitmap, e: Exception) {
                                Timber.w(e)
                                subscriber.onNext(GenerationEvent(GenerationEvent.Type.ON_FAILED_TO_GENERATE, generationFor))
                            }
                        })
                    }.build().generate()

                    subscriber.onCompleted()
                } catch (t: Throwable) {
                    subscriber.onError(t)
                }
            }
        }).subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .subscribe({ event ->
                    bus.post(event)
                }, { t ->
                    Timber.e(t)
                    bus.post(GenerationEvent(GenerationEvent.Type.ON_GENERATION_UNKNOWN_ERROR))
                }, {
                    bus.post(GenerationEvent(GenerationEvent.Type.ON_FINISH_GENERATION))
                })
    }

    @Subscribe
    fun onGenerationEvent(generationEvent: GenerationEvent) {
        val ui = ui

        if (ui == null) {
            generationInProgress = false
            Timber.d("onGenerationEvent ui == null")
            return
        }

        when (generationEvent.type) {
            GenerationPresenterImpl.GenerationEvent.Type.ON_START_GENERATION -> ui.onStartGeneration()
            GenerationPresenterImpl.GenerationEvent.Type.ON_GENERATION_UNKNOWN_ERROR -> {
                generationInProgress = false
                ui.onGenerationUnknownError()
            }
            GenerationPresenterImpl.GenerationEvent.Type.ON_FINISH_GENERATION -> {
                generationInProgress = false
                ui.onFinishGeneration()
            }
            GenerationPresenterImpl.GenerationEvent.Type.ON_GENERATED -> ui.onGenerated(generationEvent.imageParams!!, generationEvent.imageFile!!)
            GenerationPresenterImpl.GenerationEvent.Type.ON_FAILED_TO_GENERATE -> ui.onFailedToGenerate(generationEvent.imageParams!!)
        }
    }
}