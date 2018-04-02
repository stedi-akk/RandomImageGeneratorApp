package com.stedi.randomimagegenerator.app.view.dialogs.base

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatDialogFragment
import com.stedi.randomimagegenerator.app.other.getApp

abstract class BaseDialogFragment : AppCompatDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onDestroyView() {
        if (dialog != null && retainInstance) {
            dialog.setDismissMessage(null)
        }
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.apply { getApp().leakWatcher.watch(this@BaseDialogFragment) }
    }

    fun show(manager: FragmentManager) {
        show(manager, javaClass.simpleName)
    }
}