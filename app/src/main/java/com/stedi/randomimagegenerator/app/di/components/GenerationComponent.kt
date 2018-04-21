package com.stedi.randomimagegenerator.app.di.components

import com.stedi.randomimagegenerator.app.di.modules.GenerationModule
import com.stedi.randomimagegenerator.app.view.dialogs.ColoredNoiseParamsDialog
import com.stedi.randomimagegenerator.app.view.dialogs.SimpleIntegerParamsDialog
import com.stedi.randomimagegenerator.app.view.fragments.*
import dagger.Subcomponent

@Subcomponent(modules = [(GenerationModule::class)])
interface GenerationComponent {
    fun inject(fragment: ChooseGeneratorFragment)

    fun inject(dialog: SimpleIntegerParamsDialog)

    fun inject(dialog: ColoredNoiseParamsDialog)

    fun inject(fragment: ChooseEffectFragment)

    fun inject(fragment: ChooseSizeAndCountFragment)

    fun inject(fragment: ChooseSaveOptionsFragment)

    fun inject(fragment: ApplyGenerationFragment)
}