package com.stedi.randomimagegenerator.app.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.FlatColorParams;
import com.stedi.randomimagegenerator.app.other.Utils;

import java.util.List;

public class DatabasePresetRepository implements PresetRepository {
    private final SparseArray<Preset> fakeDatabase = new SparseArray<>();

    {
        Preset[] fakePresets = new Preset[]{new Preset("Fake 1", new FlatColorParams(), Quality.jpg(100), "/sdcard/"),
                new Preset("Fake 2", new FlatColorParams(), Quality.jpg(100), "/sdcard/"),
                new Preset("Fake 3", new FlatColorParams(), Quality.jpg(100), "/sdcard/")};
        for (Preset fakePreset : fakePresets) {
            fakePreset.setId(fakeDatabase.size() + 1);
            fakePreset.setTimestamp(System.currentTimeMillis());
            fakeDatabase.put(fakePreset.getId(), fakePreset);
        }
    }

    @Override
    public synchronized boolean save(@NonNull Preset preset) throws Exception {
        Utils.sleep(100);
        preset.setId(fakeDatabase.size() + 1);
        preset.setTimestamp(System.currentTimeMillis());
        fakeDatabase.put(preset.getId(), preset);
        return true;
    }

    @Override
    public synchronized boolean remove(int id) throws Exception {
        Utils.sleep(100);
        fakeDatabase.remove(id);
        return true;
    }

    @Override
    @Nullable
    public synchronized Preset get(int id) throws Exception {
        Utils.sleep(100);
        return fakeDatabase.get(id);
    }

    @Override
    @NonNull
    public synchronized List<Preset> getAll() throws Exception {
        Utils.sleep(100);
        return Utils.sparseArrayToList(fakeDatabase);
    }
}
