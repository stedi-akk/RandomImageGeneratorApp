package com.stedi.randomimagegenerator.app.view.fragments.base

import com.stedi.randomimagegenerator.app.other.getApp
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity
import com.stedi.randomimagegenerator.app.view.components.RequireViewModel

abstract class BaseFragment : LifeCycleFragment(), RequireViewModel {

    fun checkForPermission(permission: String, requestCode: Int): Boolean {
        val activity = activity ?: return false
        return (activity as BaseActivity).checkForPermission(permission, requestCode)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.apply { getApp().leakWatcher.watch(this) }
    }
}