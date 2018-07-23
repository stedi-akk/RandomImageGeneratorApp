package com.stedi.randomimagegenerator.app.view.fragments.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stedi.randomimagegenerator.app.R
import com.stepstone.stepper.BlockingStep
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError

// for lazy GenerationFragment creation with StepperLayout
class StubGenerationFragment : BaseFragment(), BlockingStep {

    companion object {
        private const val CLAZZ_NAME_KEY = "CLAZZ_NAME_KEY"

        private var hasVisibleFragment = false

        fun of(clazz: Class<out GenerationFragment>): StubGenerationFragment {
            return StubGenerationFragment().apply {
                arguments = Bundle().apply {
                    putString(CLAZZ_NAME_KEY, clazz.name)
                }
            }
        }
    }

    private lateinit var clazzName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clazzName = arguments!!.getString(CLAZZ_NAME_KEY)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.stub_step_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!hasVisibleFragment) {
            // if nothing is visible, let the first fragment be visible
            hasVisibleFragment = true
            userVisibleHint = true
        } else {
            // there is another fragment visible
            userVisibleHint = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hasVisibleFragment = false
    }

    override fun onSelected() {
        // update currently visible fragment
        hasVisibleFragment = true
        userVisibleHint = true
        getGenerationFragment()?.onSelected()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && this::clazzName.isInitialized) {
            if (getGenerationFragment() == null) {
                childFragmentManager.beginTransaction()
                        ?.add(R.id.stub_step_fragment_container, instantiate(activity, clazzName), clazzName)
                        ?.commitNow()
            }
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

    private fun getGenerationFragment(): GenerationFragment? = childFragmentManager.findFragmentByTag(clazzName) as GenerationFragment?
}