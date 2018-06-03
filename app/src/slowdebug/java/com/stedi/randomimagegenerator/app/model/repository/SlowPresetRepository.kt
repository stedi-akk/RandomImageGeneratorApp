package com.stedi.randomimagegenerator.app.model.repository

import com.stedi.randomimagegenerator.app.PRESET_REPOSITORY_SLEEP
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.other.sleep

class SlowPresetRepository(private val target: PresetRepository) : PresetRepository {

    override fun save(preset: Preset) {
        sleep(PRESET_REPOSITORY_SLEEP)
        target.save(preset)
    }

    override fun remove(id: Int) {
        sleep(PRESET_REPOSITORY_SLEEP)
        target.remove(id)
    }

    override fun get(id: Int): Preset? {
        sleep(PRESET_REPOSITORY_SLEEP)
        return target.get(id)
    }

    override fun getAll(): List<Preset> {
        sleep(PRESET_REPOSITORY_SLEEP)
        return target.getAll()
    }
}