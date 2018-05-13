package com.stedi.randomimagegenerator.app.view.fragments.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import timber.log.Timber

abstract class LifeCycleFragment : Fragment() {
    private val LOG = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (LOG) Timber.i("onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (LOG) Timber.i("onCreate")
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (LOG) Timber.i("onCreateView")
        return null
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (LOG) Timber.i("onViewCreated")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (LOG) Timber.i("onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        if (LOG) Timber.i("onStart")
    }

    override fun onResume() {
        super.onResume()
        if (LOG) Timber.i("onResume")
    }

    override fun onPause() {
        super.onPause()
        if (LOG) Timber.i("onPause")
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        if (LOG) Timber.i("onSaveInstanceState")
    }

    override fun onStop() {
        super.onStop()
        if (LOG) Timber.i("onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (LOG) Timber.i("onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (LOG) Timber.i("onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        if (LOG) Timber.i("onDetach")
    }
}