package com.stedi.randomimagegenerator.app.model.repository

import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.other.sleep

class SlowPresetRepository(private val target: PresetRepository) : PresetRepository {

    override fun save(preset: Preset) {
        sleep(3000)
        target.save(preset)
    }

    override fun remove(id: Int) {
        sleep(3000)
        target.remove(id)
    }

    override fun get(id: Int): Preset? {
        sleep(3000)
        return target.get(id)
    }

    override fun getAll(): MutableList<Preset> {
        sleep(3000)
        return target.all
    }
}