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
import rx.Completable
import rx.Scheduler
import rx.functions.Action0
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class GenerationPresenterImpl @Inject constructor(
        @DefaultScheduler private val subscribeOn: Scheduler,
        @UiScheduler private val observeOn: Scheduler,
        private val bus: LockedBus) : GenerationPresenter {

    private var ui: GenerationPresenter.UIImpl? = null
    private var generationInProgress: Boolean = false

    class Event(val type: Type, val imageParams: ImageParams? = null, val imageFile: File? = null) {
        enum class Type {
            ON_START_GENERATION,
            ON_GENERATION_UNKNOWN_ERROR,
            ON_FINISH_GENERATION,
            ON_GENERATED,
            ON_FAILED_TO_GENERATE
        }
    }

    override fun startGeneration(preset: Preset) {
        if (generationInProgress) {
            return
        }
        generationInProgress = true

        Timber.d("GENERATION STARTED")
        bus.post(Event(Event.Type.ON_START_GENERATION))

        Completable.fromAction(object : Action0 {
            private var generationFor: ImageParams? = null

            override fun call() {
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
                            Timber.e(e)
                            post(Event(Event.Type.ON_FAILED_TO_GENERATE, imageParams))
                        }
                    })

                    setFileSaveCallback(object : SaveCallback {
                        override fun onSaved(bitmap: Bitmap, file: File) {
                            val generationForRef = generationFor
                            post(Event(Event.Type.ON_GENERATED, generationForRef, file))
                        }

                        override fun onFailedToSave(bitmap: Bitmap, e: Exception) {
                            Timber.e(e)
                            val generationForRef = generationFor
                            post(Event(Event.Type.ON_FAILED_TO_GENERATE, generationForRef))
                        }
                    })
                }.build().generate()
            }

            private fun post(event: Event) {
                Completable.fromAction { bus.post(event) }.subscribeOn(observeOn).subscribe()
            }

        }).subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .subscribe({
                    Timber.d("GENERATION FINISHED")
                    bus.post(Event(Event.Type.ON_FINISH_GENERATION))
                }, { throwable ->
                    Timber.e(throwable)
                    bus.post(Event(Event.Type.ON_GENERATION_UNKNOWN_ERROR))
                })
    }

    private val busTarget: Any = object : Any() {
        @Subscribe
        fun onEvent(event: Event) {
            val ui = ui
            if (ui == null) {
                generationInProgress = false
                Timber.d("busTarget onEvent ui == null")
                return
            }

            when (event.type) {
                GenerationPresenterImpl.Event.Type.ON_START_GENERATION -> ui.onStartGeneration()
                GenerationPresenterImpl.Event.Type.ON_GENERATION_UNKNOWN_ERROR -> {
                    generationInProgress = false
                    ui.onGenerationUnknownError()
                }
                GenerationPresenterImpl.Event.Type.ON_FINISH_GENERATION -> {
                    generationInProgress = false
                    ui.onFinishGeneration()
                }
                GenerationPresenterImpl.Event.Type.ON_GENERATED -> ui.onGenerated(event.imageParams!!, event.imageFile!!)
                GenerationPresenterImpl.Event.Type.ON_FAILED_TO_GENERATE -> ui.onFailedToGenerate(event.imageParams!!)
            }
        }
    }

    override fun onAttach(ui: GenerationPresenter.UIImpl) {
        this.ui = ui
        bus.register(busTarget)
    }

    override fun onDetach() {
        bus.unregister(busTarget)
        ui = null
    }
}