package com.stedi.randomimagegenerator.app.view.fragments.base

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stedi.randomimagegenerator.app.R
import com.stepstone.stepper.BlockingStep
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import timber.log.Timber

// for lazy GenerationFragment creation (with StepperLayout)
class StubGenerationFragment : BaseFragment(), BlockingStep {

    private val FRAGMENT_SHOW_DELAY = 200L

    companion object {
        private const val KEY_CLAZZ_NAME = "KEY_CLAZZ_NAME"

        fun of(clazz: Class<out GenerationFragment>): StubGenerationFragment {
            return StubGenerationFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_CLAZZ_NAME, clazz.name)
                }
            }
        }
    }

    private lateinit var clazzName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clazzName = arguments?.getString(KEY_CLAZZ_NAME) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.stub_step_fragment, container, false)
    }

    override fun onSelected() {
        val fragment = getGenerationFragment()
        if (fragment == null) {
            Handler().postDelayed({
                if (isResumed && getGenerationFragment() == null) {
                    try {
                        childFragmentManager.beginTransaction()
                                ?.add(R.id.stub_step_fragment_container, instantiate(activity, clazzName), clazzName)
                                ?.commitNow()
                        getGenerationFragment()?.onSelected()
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            }, FRAGMENT_SHOW_DELAY)
        } else {
            fragment.onSelected()
        }
    }

    override fun verifyStep(): VerificationError? {
        return getGenerationFragment()?.verifyStep()
    }

    override fun onError(error: VerificationError) {
        getGenerationFragment()?.onError(error)
    }

    override fun onBackClicked(callback: StepperLayout.OnBackClickedCallback) {
        getGenerationFragment()?.onBackClicked(callback)
    }

    override fun onCompleteClicked(callback: StepperLayout.OnCompleteClickedCallback) {
        getGenerationFragment()?.onCompleteClicked(callback)
    }

    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback) {
        getGenerationFragment()?.onNextClicked(callback)
    }

    private fun getGenerationFragment(): GenerationFragment? {
        return childFragmentManager.findFragmentByTag(clazzName) as GenerationFragment?
    }
}