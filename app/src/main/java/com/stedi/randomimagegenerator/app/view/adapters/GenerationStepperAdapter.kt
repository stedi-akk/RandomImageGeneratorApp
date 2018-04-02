package com.stedi.randomimagegenerator.app.view.adapters

import android.content.Context
import android.support.annotation.IntRange
import android.support.v4.app.FragmentManager
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.view.fragments.*
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel

class GenerationStepperAdapter(fm: FragmentManager, context: Context) : AbstractFragmentStepAdapter(fm, context) {

    override fun getCount() = 5

    override fun createStep(@IntRange(from = 0L) position: Int): Step {
        return when (position) {
            0 -> ChooseGeneratorFragment()
            1 -> ChooseEffectFragment()
            2 -> ChooseSizeAndCountFragment()
            3 -> ChooseSaveOptionsFragment()
            4 -> ApplyGenerationFragment()
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
                    .setEndButtonLabel(R.string.quality).create()
            3 -> builder.setBackButtonLabel(R.string.size_count)
                    .setEndButtonLabel(R.string.summary).create()
            4 -> builder.setBackButtonLabel(R.string.configure)
                    .setEndButtonLabel(R.string.summary)
                    .setEndButtonVisible(false).create()
            else -> throw IllegalStateException("unreachable code")
        }
    }
}