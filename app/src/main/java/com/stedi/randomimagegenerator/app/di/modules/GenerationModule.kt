package com.stedi.randomimagegenerator.app.di.modules

import com.stedi.randomimagegenerator.app.di.DefaultScheduler
import com.stedi.randomimagegenerator.app.di.RigScheduler
import com.stedi.randomimagegenerator.app.di.RootSavePath
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.other.logger.Logger
import com.stedi.randomimagegenerator.app.presenter.impl.*
import com.stedi.randomimagegenerator.app.presenter.interfaces.*
import dagger.Module
import dagger.Provides
import rx.Scheduler

@Module
class GenerationModule {

    @Provides
    fun provideGenerationStepsPresenter(pendingPreset: PendingPreset, logger: Logger): GenerationStepsPresenter {
        return GenerationStepsPresenterImpl(pendingPreset, logger)
    }

    @Provides
    fun provideChooseGeneratorPresenter(pendingPreset: PendingPreset, logger: Logger): ChooseGeneratorPresenter {
        return ChooseGeneratorPresenterImpl(pendingPreset, logger)
    }

    @Provides
    fun provideEditColoredCirclesPresenter(pendingPreset: PendingPreset, logger: Logger): SimpleIntegerParamsPresenter {
        return SimpleIntegerParamsPresenterImpl(pendingPreset, logger)
    }

    @Provides
    fun provideColoredNoiseParamsPresenter(pendingPreset: PendingPreset): ColoredNoiseParamsPresenter {
        return ColoredNoiseParamsPresenterImpl(pendingPreset)
    }

    @Provides
    fun provideChooseEffectPresenter(pendingPreset: PendingPreset, logger: Logger): ChooseEffectPresenter {
        return ChooseEffectPresenterImpl(pendingPreset, logger)
    }

    @Provides
    fun provideChooseSizeAndCountPresenter(pendingPreset: PendingPreset, logger: Logger): ChooseSizeAndCountPresenter {
        return ChooseSizeAndCountPresenterImpl(pendingPreset, logger)
    }

    @Provides
    fun provideChooseSaveOptionsPresenter(pendingPreset: PendingPreset, logger: Logger): ChooseSaveOptionsPresenter {
        return ChooseSaveOptionsPresenterImpl(pendingPreset, logger)
    }

    @Provides
    fun provideApplyGenerationPresenter(pendingPreset: PendingPreset,
                                        presetRepository: PresetRepository,
                                        @RootSavePath rootSavePath: String,
                                        @RigScheduler superSubscribeOn: Scheduler,
                                        @DefaultScheduler subscribeOn: Scheduler,
                                        @UiScheduler observeOn: Scheduler,
                                        bus: CachedBus, logger: Logger): ApplyGenerationPresenter {
        return ApplyGenerationPresenterImpl(pendingPreset, presetRepository, rootSavePath, superSubscribeOn, subscribeOn, observeOn, bus, logger)
    }
}