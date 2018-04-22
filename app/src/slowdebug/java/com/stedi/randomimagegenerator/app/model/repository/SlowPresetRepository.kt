package com.stedi.randomimagegenerator.app.model.repository

import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.other.sleep

class SlowPresetRepository(private val target: PresetRepository) : PresetRepository {
    private val sleepTime = 4000L

    override fun save(preset: Preset) {
        sleep(sleepTime)
        target.save(preset)
    }

    override fun remove(id: Int) {
        sleep(sleepTime)
        target.remove(id)
    }

    override fun get(id: Int): Preset? {
        sleep(sleepTime)
        return target.get(id)
    }

    override fun getAll(): List<Preset> {
        sleep(sleepTime)
        return target.getAll()
    }
}