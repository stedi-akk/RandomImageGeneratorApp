package com.stedi.randomimagegenerator.app.model.repository;

import android.util.LongSparseArray;

import com.stedi.randomimagegenerator.app.model.data.Preset;

import java.util.ArrayList;
import java.util.List;

public class CachedPresetRepository implements PresetRepository {
    private final LongSparseArray<Preset> cache = new LongSparseArray<>();

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
        Preset preset = cache.get(id);
        if (preset != null || isActual)
            return preset;
        preset = target.get(id);
        cache.put(id, preset);
        return preset;
    }

    @Override
    public List<Preset> getAll() {
        if (isActual) {
            List<Preset> result = new ArrayList<>();
            for (int i = 0; i < cache.size(); i++)
                result.add(cache.valueAt(i));
            return result;
        } else {
            List<Preset> result = target.getAll();
            for (Preset preset : result)
                cache.put(preset.getId(), preset);
            isActual = true;
            return result;
        }
    }
}
