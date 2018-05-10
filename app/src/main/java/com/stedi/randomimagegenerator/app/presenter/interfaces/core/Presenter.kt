package com.stedi.randomimagegenerator.app.presenter.interfaces.core

import java.io.Serializable

interface UI

interface Presenter<in T : UI> {
    fun onAttach(ui: T)

    fun onDetach()

    fun onDestroy() {}
}

interface RetainedPresenter<in T : UI> : Presenter<T> {
    fun onRestore(state: Serializable)

    fun onRetain(): Serializable?
}