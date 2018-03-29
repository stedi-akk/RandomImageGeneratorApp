package com.stedi.randomimagegenerator.app.model.repository

import android.support.annotation.VisibleForTesting
import android.util.SparseArray
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.other.toList

class CachedPresetRepository(private val target: PresetRepository) : PresetRepository {
    private val cache = SparseArray<Preset>()

    private var isActual = false

    @Throws(Exception::class)
    @Synchronized
    override fun save(preset: Preset) {
        target.save(preset)
        cache.put(preset.id, preset)
    }

    @Throws(Exception::class)
    @Synchronized
    override fun remove(id: Int) {
        target.remove(id)
        cache.remove(id)
    }

    @Throws(Exception::class)
    @Synchronized
    override fun get(id: Int): Preset? {
        if (cache.indexOfKey(id) >= 0) {
            return cache.get(id)
        } else {
            val preset = target.get(id)
            if (preset != null) {
                cache.put(id, preset)
            }
            return preset
        }
    }

    @Throws(Exception::class)
    @Synchronized
    override fun getAll(): List<Preset> {
        if (isActual) {
            return cache.toList()
        } else {
            val result = target.getAll()
            for (preset in result) {
                cache.put(preset.id, preset)
            }
            isActual = true
            return result
        }
    }

    @VisibleForTesting
    @Synchronized
    fun getCache() = cache
}