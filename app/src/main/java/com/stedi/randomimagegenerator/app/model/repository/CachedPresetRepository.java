package com.stedi.randomimagegenerator.app.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.Utils;

import java.util.List;

public class CachedPresetRepository implements PresetRepository {
    private final SparseArray<Preset> cache = new SparseArray<>();

    private final PresetRepository target;

    private boolean isActual;

    public CachedPresetRepository(@NonNull PresetRepository target) {
        this.target = target;
    }

    @Override
    public synchronized boolean save(@NonNull Preset preset) throws Exception {
        if (target.save(preset)) {
            cache.put(preset.getId(), preset);
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean remove(int id) throws Exception {
        if (target.remove(id)) {
            cache.remove(id);
            return true;
        }
        return false;
    }

    @Override
    @Nullable
    public synchronized Preset get(int id) throws Exception {
        if (cache.indexOfKey(id) >= 0) {
            return cache.get(id);
        } else {
            Preset preset = target.get(id);
            cache.put(id, preset);
            return preset;
        }
    }

    @Override
    @NonNull
    public synchronized List<Preset> getAll() throws Exception {
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
