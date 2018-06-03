package com.stedi.randomimagegenerator.app.di.modules

import android.content.Context
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.di.AppContext
import com.stedi.randomimagegenerator.app.di.DefaultScheduler
import com.stedi.randomimagegenerator.app.di.SaveFolder
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository
import com.stedi.randomimagegenerator.app.other.LockedBus
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
    fun provideApplyGenerationPresenter(@AppContext context: Context, pendingPreset: PendingPreset, @SaveFolder saveFolder: String, presetRepository: PresetRepository,
                                        @DefaultScheduler subscribeOn: Scheduler, @UiScheduler observeOn: Scheduler, bus: LockedBus): ApplyGenerationPresenter {
        return ApplyGenerationPresenterImpl(
                pendingPreset,
                presetRepository,
                saveFolder,
                context.resources.getString(R.string.unsaved_preset_name),
                subscribeOn, observeOn, bus)
    }
}