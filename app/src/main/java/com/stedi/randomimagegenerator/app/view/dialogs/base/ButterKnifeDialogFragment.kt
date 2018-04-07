package com.stedi.randomimagegenerator.app.view.dialogs.base

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import butterknife.ButterKnife
import butterknife.Unbinder

abstract class ButterKnifeDialogFragment : BaseDialogFragment() {
    private var unbinder: Unbinder? = null

    protected fun inflateAndBind(@LayoutRes layoutResId: Int): View {
        val view = LayoutInflater.from(context).inflate(layoutResId, null)
        unbinder = ButterKnife.bind(this, view)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
    }
}