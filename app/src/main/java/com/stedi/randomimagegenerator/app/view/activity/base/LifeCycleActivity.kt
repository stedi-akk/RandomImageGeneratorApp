package com.stedi.randomimagegenerator.app.view.activity.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import timber.log.Timber

abstract class LifeCycleActivity : AppCompatActivity() {
    private val LOG = false

    override fun onCreate(savedInstanceState: Bundle?) {
        if (LOG) Timber.i("onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onRestart() {
        if (LOG) Timber.i("onRestart")
        super.onRestart()
    }

    override fun onStart() {
        if (LOG) Timber.i("onStart")
        super.onStart()
    }

    override fun onResume() {
        if (LOG) Timber.i("onResume")
        super.onResume()
    }

    override fun onPause() {
        if (LOG) Timber.i("onPause")
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (LOG) Timber.i("onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        if (LOG) Timber.i("onStop")
        super.onStop()
    }

    override fun onDestroy() {
        if (LOG) Timber.i("onDestroy")
        super.onDestroy()
    }
}