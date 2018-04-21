package com.stedi.randomimagegenerator.app.di.modules

import android.content.Context
import android.os.Environment
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.di.AppContext
import com.stedi.randomimagegenerator.app.di.DefaultScheduler
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.presenter.impl.*
import com.stedi.randomimagegenerator.app.presenter.interfaces.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import rx.Scheduler

@Module(includes = [(GenerationModule.Declarations::class)])
class GenerationModule {
    @Module
    interface Declarations {
        @Binds
        fun provideChooseGeneratorPresenter(presenter: ChooseGeneratorPresenterImpl): ChooseGeneratorPresenter

        @Binds
        fun provideChooseEffectPresenter(presenter: ChooseEffectPresenterImpl): ChooseEffectPresenter

        @Binds
        fun provideChooseSizeAndCountPresenter(presenter: ChooseSizeAndCountPresenterImpl): ChooseSizeAndCountPresenter

        @Binds
        fun provideChooseSaveOptionsPresenter(presenter: ChooseSaveOptionsPresenterImpl): ChooseSaveOptionsPresenter
    }

    @Provides
    fun provideApplyGenerationPresenter(@AppContext context: Context, pendingPreset: PendingPreset, presetRepository: PresetRepository,
                                        @DefaultScheduler subscribeOn: Scheduler, @UiScheduler observeOn: Scheduler, bus: CachedBus): ApplyGenerationPresenter {
        return ApplyGenerationPresenterImpl(
                pendingPreset,
                presetRepository,
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).resolve("RIG").path,
                context.resources.getString(R.string.unsaved_preset_name),
                subscribeOn, observeOn, bus)
    }
}