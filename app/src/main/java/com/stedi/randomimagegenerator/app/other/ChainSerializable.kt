package com.stedi.randomimagegenerator.app.other

import java.io.Serializable

class ChainSerializable(private val state: Serializable?) : Serializable {
    private var next: ChainSerializable? = null

    fun addChain(state: Serializable?) = ChainSerializable(state).apply { next = this }

    fun getChain() = next

    fun get() = state
}