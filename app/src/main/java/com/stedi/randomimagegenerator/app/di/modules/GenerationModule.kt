package com.stedi.randomimagegenerator.app.di.modules

import com.stedi.randomimagegenerator.app.presenter.impl.*
import com.stedi.randomimagegenerator.app.presenter.interfaces.*
import dagger.Binds
import dagger.Module

@Module(includes = [(GenerationModule.Declarations::class)])
class GenerationModule {
    @Module
    interface Declarations {
        @Binds
        fun provideGenerationStepsPresenter(presenter: GenerationStepsPresenterImpl): GenerationStepsPresenter

        @Binds
        fun provideChooseGeneratorPresenter(presenter: ChooseGeneratorPresenterImpl): ChooseGeneratorPresenter

        @Binds
        fun provideEditColoredCirclesPresenter(presenter: SimpleIntegerParamsPresenterImpl): SimpleIntegerParamsPresenter

        @Binds
        fun provideColoredNoiseParamsPresenter(presenter: ColoredNoiseParamsPresenterImpl): ColoredNoiseParamsPresenter

        @Binds
        fun provideChooseEffectPresenter(presenter: ChooseEffectPresenterImpl): ChooseEffectPresenter

        @Binds
        fun provideChooseSizeAndCountPresenter(presenter: ChooseSizeAndCountPresenterImpl): ChooseSizeAndCountPresenter

        @Binds
        fun provideChooseSaveOptionsPresenter(presenter: ChooseSaveOptionsPresenterImpl): ChooseSaveOptionsPresenter

        @Binds
        fun provideApplyGenerationPresenter(presenter: ApplyGenerationPresenterImpl): ApplyGenerationPresenter
    }
}