package com.stedi.randomimagegenerator.app.view.activity.base

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.stedi.randomimagegenerator.app.di.components.ActivityComponent
import com.stedi.randomimagegenerator.app.di.modules.ActivityModule
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.other.getApp
import javax.inject.Inject

abstract class BaseActivity : LifeCycleActivity() {

    protected val component: ActivityComponent by lazy {
        getApp().component.plus(ActivityModule(this))
    }

    @Inject
    lateinit var pendingPreset: PendingPreset
    @Inject
    lateinit var bus: CachedBus

    private companion object {
        var mustRestorePendingPreset = true
    }

    class PermissionEvent(val permission: String, val requestCode: Int, val isGranted: Boolean)

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        if (mustRestorePendingPreset && savedInstanceState != null) {
            pendingPreset.restore(savedInstanceState)
        }
        mustRestorePendingPreset = false
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        bus.unlock()
    }

    override fun onPause() {
        super.onPause()
        bus.lock()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        pendingPreset.retain(outState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val isGranted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        bus.postDeadEvent(PermissionEvent(permissions[0], requestCode, isGranted))
    }

    fun checkForPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            return false
        }
        return true
    }
}