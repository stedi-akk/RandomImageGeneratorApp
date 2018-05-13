package com.stedi.randomimagegenerator.app.di.components

import com.stedi.randomimagegenerator.app.di.modules.GenerationModule
import com.stedi.randomimagegenerator.app.view.fragments.*
import dagger.Subcomponent

@Subcomponent(modules = [(GenerationModule::class)])
interface GenerationComponent {
    fun inject(fragmentModel: ChooseGeneratorFragmentModel)

    fun inject(fragmentModel: ChooseEffectFragmentModel)

    fun inject(fragmentModel: ChooseSizeAndCountFragmentModel)

    fun inject(fragmentModel: ChooseSaveOptionsFragmentModel)

    fun inject(fragmentModel: ApplyGenerationFragmentModel)
}