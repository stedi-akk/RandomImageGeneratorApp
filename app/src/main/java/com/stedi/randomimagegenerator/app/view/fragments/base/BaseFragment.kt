package com.stedi.randomimagegenerator.app.view.fragments.base

import com.stedi.randomimagegenerator.app.other.getApp
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity

abstract class BaseFragment : LifeCycleFragment() {

    fun getBaseActivity() = activity as BaseActivity

    fun checkForPermission(permission: String, requestCode: Int): Boolean {
        return getBaseActivity().checkForPermission(permission, requestCode)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.apply { getApp().leakWatcher.watch(this) }
    }
}