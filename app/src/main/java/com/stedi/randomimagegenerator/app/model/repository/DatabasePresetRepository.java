package com.stedi.randomimagegenerator.app.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.Utils;

import java.util.List;

public class DatabasePresetRepository implements PresetRepository {
    private final SparseArray<Preset> fakeDatabase = new SparseArray<>();

    @Override
    public boolean save(@NonNull Preset preset) throws Exception {
        Utils.sleep(100);
        fakeDatabase.put(preset.getId(), preset);
        return true;
    }

    @Override
    public boolean remove(int id) throws Exception {
        Utils.sleep(100);
        fakeDatabase.remove(id);
        return true;
    }

    @Override
    @Nullable
    public Preset get(int id) throws Exception {
        Utils.sleep(100);
        return fakeDatabase.get(id);
    }

    @Override
    @NonNull
    public List<Preset> getAll() throws Exception {
        Utils.sleep(100);
        return Utils.sparseArrayToList(fakeDatabase);
    }
}
