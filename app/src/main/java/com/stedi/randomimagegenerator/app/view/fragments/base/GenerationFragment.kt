package com.stedi.randomimagegenerator.app.view.fragments.base

import android.support.annotation.CallSuper
import com.stedi.randomimagegenerator.app.di.components.GenerationComponent
import com.stedi.randomimagegenerator.app.di.modules.GenerationModule
import com.stedi.randomimagegenerator.app.other.hideInput
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import com.stepstone.stepper.BlockingStep
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError

abstract class GenerationFragment : ButterKnifeFragment(), BlockingStep {

    val generationComponent: GenerationComponent by lazy {
        (activity as BaseActivity).activityComponent.plus(GenerationModule())
    }

    override fun verifyStep(): VerificationError? = null

    override fun onSelected() {}

    override fun onError(error: VerificationError) {}

    @CallSuper
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback) {
        activity?.hideInput()
        callback.goToNextStep()
    }

    @CallSuper
    override fun onCompleteClicked(callback: StepperLayout.OnCompleteClickedCallback) {
        callback.complete()
    }

    @CallSuper
    override fun onBackClicked(callback: StepperLayout.OnBackClickedCallback) {
        activity?.hideInput()
        callback.goToPrevStep()
    }
}