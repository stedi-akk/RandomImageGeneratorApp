package com.stedi.randomimagegenerator.app.model.repository;

import android.util.SparseArray;

import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.Utils;

import java.util.List;

public class CachedPresetRepository implements PresetRepository {
    private final SparseArray<Preset> cache = new SparseArray<>();

    private final PresetRepository target;

    private boolean isActual;

    public CachedPresetRepository(PresetRepository target) {
        this.target = target;
    }

    @Override
    public boolean save(Preset preset) {
        if (target.save(preset)) {
            cache.put(preset.getId(), preset);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(int id) {
        if (target.remove(id)) {
            cache.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public Preset get(int id) {
        if (cache.indexOfKey(id) >= 0) {
            return cache.get(id);
        } else {
            Preset preset = target.get(id);
            cache.put(id, preset);
            return preset;
        }
    }

    @Override
    public List<Preset> getAll() {
        if (isActual) {
            return Utils.sparseArrayToList(cache);
        } else {
            List<Preset> result = target.getAll();
            for (Preset preset : result)
                cache.put(preset.getId(), preset);
            isActual = true;
            return result;
        }
    }
}
