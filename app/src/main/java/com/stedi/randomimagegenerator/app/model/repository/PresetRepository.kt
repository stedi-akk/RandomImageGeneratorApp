package com.stedi.randomimagegenerator.app.model.repository

import com.stedi.randomimagegenerator.app.model.data.Preset

interface PresetRepository {
    @Throws(Exception::class)
    fun save(preset: Preset)

    @Throws(Exception::class)
    fun remove(id: Int)

    @Throws(Exception::class)
    fun get(id: Int): Preset?

    @Throws(Exception::class)
    fun getAll(): List<Preset>
}