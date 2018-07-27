package com.stedi.randomimagegenerator.app.view.adapters

import android.content.Context
import android.support.annotation.IntRange
import android.support.v4.app.FragmentManager
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.di.ActivityContext
import com.stedi.randomimagegenerator.app.view.fragments.*
import com.stedi.randomimagegenerator.app.view.fragments.base.StubGenerationFragment
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel

class GenerationStepperAdapter(fm: FragmentManager, @ActivityContext context: Context) : AbstractFragmentStepAdapter(fm, context) {

    override fun getCount() = 6

    override fun createStep(@IntRange(from = 0L) position: Int): Step {
        return when (position) {
            0 -> StubGenerationFragment.of(ChooseGeneratorFragment::class.java)
            1 -> StubGenerationFragment.of(ChooseEffectFragment::class.java)
            2 -> StubGenerationFragment.of(ChooseSizeAndCountFragment::class.java)
            3 -> StubGenerationFragment.of(ChooseColorFragment::class.java)
            4 -> StubGenerationFragment.of(ChooseSaveOptionsFragment::class.java)
            5 -> StubGenerationFragment.of(ApplyGenerationFragment::class.java)
            else -> throw IllegalStateException("unreachable code")
        }
    }

    override fun getViewModel(@IntRange(from = 0L) position: Int): StepViewModel {
        val builder = StepViewModel.Builder(context)
        return when (position) {
            0 -> builder.setEndButtonLabel(R.string.effect)
                    .setBackButtonLabel(R.string.generator)
                    .setBackButtonVisible(false).create()
            1 -> builder.setBackButtonLabel(R.string.generator)
                    .setEndButtonLabel(R.string.size_count).create()
            2 -> builder.setBackButtonLabel(R.string.effect)
                    .setEndButtonLabel(R.string.color).create()
            3 -> builder.setBackButtonLabel(R.string.size_count)
                    .setEndButtonLabel(R.string.quality).create()
            4 -> builder.setBackButtonLabel(R.string.color)
                    .setEndButtonLabel(R.string.summary).create()
            5 -> builder.setBackButtonLabel(R.string.configure)
                    .setEndButtonLabel(R.string.summary)
                    .setEndButtonVisible(false).create()
            else -> throw IllegalStateException("unreachable code")
        }
    }
}