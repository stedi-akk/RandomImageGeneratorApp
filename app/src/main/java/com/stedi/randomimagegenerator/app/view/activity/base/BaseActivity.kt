package com.stedi.randomimagegenerator.app.view.activity.base

import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.stedi.randomimagegenerator.app.di.components.ActivityComponent
import com.stedi.randomimagegenerator.app.di.modules.ActivityModule
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.other.getApp
import com.stedi.randomimagegenerator.app.view.components.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class BaseActivityModel : BaseViewModel<BaseActivity>() {
    @Inject lateinit var pendingPreset: PendingPreset
    @Inject lateinit var bus: CachedBus

    override fun onCreate(view: BaseActivity) {
        Timber.d("BaseActivityModel onCreate")
        view.activityComponent.inject(this)
    }
}

abstract class BaseActivity : LifeCycleActivity() {

    val activityComponent: ActivityComponent by lazy {
        getApp().appComponent.plus(ActivityModule(this))
    }

    private lateinit var viewModel: BaseActivityModel

    private companion object {
        const val KEY_PENDING_PRESET_STATE = "KEY_PENDING_PRESET_STATE"
    }

    class PermissionEvent(val permission: String, val requestCode: Int, val isGranted: Boolean)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(BaseActivityModel::class.java)
        viewModel.init(this)

        savedInstanceState?.apply {
            savedInstanceState.getParcelableArray(KEY_PENDING_PRESET_STATE)?.apply {
                viewModel.pendingPreset.restore(this)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.bus.unlock()
    }

    override fun onPause() {
        super.onPause()
        viewModel.bus.lock()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArray(KEY_PENDING_PRESET_STATE, viewModel.pendingPreset.retain())
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val isGranted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        viewModel.bus.postDeadEvent(PermissionEvent(permissions[0], requestCode, isGranted))
    }

    fun checkForPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            return false
        }
        return true
    }
}