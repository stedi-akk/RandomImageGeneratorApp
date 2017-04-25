package com.stedi.randomimagegenerator.app.model.repository;

import android.util.LongSparseArray;

import com.stedi.randomimagegenerator.app.model.data.Preset;

import java.util.ArrayList;
import java.util.List;

public class DatabasePresetRepository implements PresetRepository {
    private final LongSparseArray<Preset> fakeDatabase = new LongSparseArray<>();

    @Override
    public boolean save(Preset preset) {
        fakeDatabase.put(preset.getId(), preset);
        return true;
    }

    @Override
    public boolean remove(int id) {
        fakeDatabase.remove(id);
        return true;
    }

    @Override
    public Preset get(int id) {
        return fakeDatabase.get(id);
    }

    @Override
    public List<Preset> getAll() {
        List<Preset> result = new ArrayList<>();
        for (int i = 0; i < fakeDatabase.size(); i++)
            result.add(fakeDatabase.valueAt(i));
        return result;
    }
}
