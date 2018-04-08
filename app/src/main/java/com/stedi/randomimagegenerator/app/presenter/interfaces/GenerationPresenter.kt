package com.stedi.randomimagegenerator.app.presenter.interfaces

import android.Manifest
import android.graphics.Bitmap
import android.support.annotation.CallSuper
import android.support.annotation.RequiresPermission
import com.squareup.otto.Subscribe
import com.stedi.randomimagegenerator.DefaultFileNamePolicy
import com.stedi.randomimagegenerator.ImageParams
import com.stedi.randomimagegenerator.Rig
import com.stedi.randomimagegenerator.app.di.RigScheduler
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.other.logger.Logger
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.RetainedPresenter
import com.stedi.randomimagegenerator.app.presenter.interfaces.core.UI
import com.stedi.randomimagegenerator.callbacks.GenerateCallback
import com.stedi.randomimagegenerator.callbacks.SaveCallback
import rx.Completable
import rx.Scheduler
import rx.functions.Action0
import java.io.File
import java.io.Serializable

abstract class GenerationPresenter<in T : GenerationPresenter.UIImpl>(
        @RigScheduler private val subscribeOn: Scheduler,
        @UiScheduler private val observeOn: Scheduler,
        private val bus: CachedBus,
        private val logger: Logger) : RetainedPresenter<T> {

    private var ui: UIImpl? = null
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

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    open fun startGeneration(preset: Preset) {
        if (generationInProgress) {
            return
        }
        generationInProgress = true

        logger.log(this, "GENERATION STARTED")
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
                            logger.log(this@GenerationPresenter, e)
                            post(Event(Event.Type.ON_FAILED_TO_GENERATE, imageParams))
                        }
                    })

                    setFileSaveCallback(object : SaveCallback {
                        override fun onSaved(bitmap: Bitmap, file: File) {
                            val generationForRef = generationFor
                            post(Event(Event.Type.ON_GENERATED, generationForRef, file))
                        }

                        override fun onFailedToSave(bitmap: Bitmap, e: Exception) {
                            logger.log(this@GenerationPresenter, e)
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
                    logger.log(this, "GENERATION FINISHED")
                    bus.post(Event(Event.Type.ON_FINISH_GENERATION))
                }, { throwable ->
                    logger.log(this, throwable)
                    bus.post(Event(Event.Type.ON_GENERATION_UNKNOWN_ERROR))
                })
    }

    private val busTarget = object : Any() {
        @Subscribe
        fun onEvent(event: Event) {
            val ui = ui
            if (ui == null) {
                generationInProgress = false
                logger.log(this@GenerationPresenter, "busTarget onEvent ui == null")
                return
            }

            when (event.type) {
                GenerationPresenter.Event.Type.ON_START_GENERATION -> ui.onStartGeneration()
                GenerationPresenter.Event.Type.ON_GENERATION_UNKNOWN_ERROR -> {
                    generationInProgress = false
                    ui.onGenerationUnknownError()
                }
                GenerationPresenter.Event.Type.ON_FINISH_GENERATION -> {
                    generationInProgress = false
                    ui.onFinishGeneration()
                }
                GenerationPresenter.Event.Type.ON_GENERATED -> ui.onGenerated(event.imageParams!!, event.imageFile!!)
                GenerationPresenter.Event.Type.ON_FAILED_TO_GENERATE -> ui.onFailedToGenerate(event.imageParams!!)
            }
        }
    }

    @CallSuper
    override fun onAttach(ui: T) {
        this.ui = ui
        bus.register(busTarget)
    }

    @CallSuper
    override fun onDetach() {
        bus.unregister(busTarget)
        ui = null
    }

    @CallSuper
    override fun onRestore(state: Serializable) {
        generationInProgress = state as Boolean
    }

    @CallSuper
    override fun onRetain(): Serializable? {
        return generationInProgress
    }

    interface UIImpl : UI {
        fun onStartGeneration()

        fun onGenerated(imageParams: ImageParams, imageFile: File)

        fun onGenerationUnknownError()

        fun onFailedToGenerate(imageParams: ImageParams)

        fun onFinishGeneration()
    }
}