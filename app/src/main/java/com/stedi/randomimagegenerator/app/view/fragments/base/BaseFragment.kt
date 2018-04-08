package com.stedi.randomimagegenerator.app.view.fragments.base

import com.stedi.randomimagegenerator.app.other.getApp
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity

abstract class BaseFragment : LifeCycleFragment() {

    fun checkForPermission(permission: String, requestCode: Int): Boolean {
        val activity = activity ?: return false
        return (activity as BaseActivity).checkForPermission(permission, requestCode)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.apply { getApp().leakWatcher.watch(this) }
    }
}