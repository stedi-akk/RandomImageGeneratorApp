package com.stedi.randomimagegenerator.app.view.components

import android.arch.lifecycle.ViewModel

abstract class BaseViewModel<in V> : ViewModel() {
    var isInitialized: Boolean = false

    abstract fun onCreate(view: V)

    fun init(view: V) {
        if (!isInitialized) {
            onCreate(view)
            isInitialized = true
        }
    }
}