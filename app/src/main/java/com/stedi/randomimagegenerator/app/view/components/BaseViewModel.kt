package com.stedi.randomimagegenerator.app.view.components

import android.arch.lifecycle.ViewModel
import timber.log.Timber

interface RequireViewModel

abstract class BaseViewModel<in V : RequireViewModel> : ViewModel() {
    var isInitialized: Boolean = false

    abstract fun onCreate(view: V)

    fun init(view: V) {
        if (!isInitialized) {
            Timber.d("${this.javaClass.simpleName} onCreate")
            onCreate(view)
            isInitialized = true
        }
    }

    override fun onCleared() {
        Timber.d("${this.javaClass.simpleName} onCleared")
    }
}