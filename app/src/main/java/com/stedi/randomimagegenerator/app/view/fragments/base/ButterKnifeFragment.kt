package com.stedi.randomimagegenerator.app.view.fragments.base

import android.os.Bundle
import android.view.View
import butterknife.ButterKnife
import butterknife.Unbinder

abstract class ButterKnifeFragment : BaseFragment() {
    private var unbinder: Unbinder? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unbinder = ButterKnife.bind(this, view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
    }
}