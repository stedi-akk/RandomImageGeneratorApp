package com.stedi.randomimagegenerator.app.view.fragments.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stedi.randomimagegenerator.app.other.getApp
import com.stedi.randomimagegenerator.app.other.logger.Logger
import javax.inject.Inject

abstract class LifeCycleFragment : Fragment() {
    private val LOG = false

    @Inject lateinit var lifeCycleLogger: Logger

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (LOG) {
            context.getApp().component.inject(this)
            lifeCycleLogger.log(this, "onAttach")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (LOG) lifeCycleLogger.log(this, "onCreate")
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (LOG) lifeCycleLogger.log(this, "onCreateView")
        return null
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (LOG) lifeCycleLogger.log(this, "onViewCreated")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (LOG) lifeCycleLogger.log(this, "onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        if (LOG) lifeCycleLogger.log(this, "onStart")
    }

    override fun onResume() {
        super.onResume()
        if (LOG) lifeCycleLogger.log(this, "onResume")
    }

    override fun onPause() {
        super.onPause()
        if (LOG) lifeCycleLogger.log(this, "onPause")
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        if (LOG) lifeCycleLogger.log(this, "onSaveInstanceState")
    }

    override fun onStop() {
        super.onStop()
        if (LOG) lifeCycleLogger.log(this, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (LOG) lifeCycleLogger.log(this, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (LOG) lifeCycleLogger.log(this, "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        if (LOG) lifeCycleLogger.log(this, "onDetach")
    }
}