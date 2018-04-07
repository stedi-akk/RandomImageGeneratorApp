package com.stedi.randomimagegenerator.app.view.activity.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stedi.randomimagegenerator.app.other.getApp
import com.stedi.randomimagegenerator.app.other.logger.Logger
import javax.inject.Inject

abstract class LifeCycleActivity : AppCompatActivity() {
    private val LOG = false

    @Inject lateinit var lifeCycleLogger: Logger

    override fun onCreate(savedInstanceState: Bundle?) {
        if (LOG) {
            getApp().component.inject(this)
            lifeCycleLogger.log(this, "onCreate")
        }
        super.onCreate(savedInstanceState)
    }

    override fun onRestart() {
        if (LOG) lifeCycleLogger.log(this, "onRestart")
        super.onRestart()
    }

    override fun onStart() {
        if (LOG) lifeCycleLogger.log(this, "onStart")
        super.onStart()
    }

    override fun onResume() {
        if (LOG) lifeCycleLogger.log(this, "onResume")
        super.onResume()
    }

    override fun onPause() {
        if (LOG) lifeCycleLogger.log(this, "onPause")
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (LOG) lifeCycleLogger.log(this, "onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        if (LOG) lifeCycleLogger.log(this, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        if (LOG) lifeCycleLogger.log(this, "onDestroy")
        super.onDestroy()
    }
}