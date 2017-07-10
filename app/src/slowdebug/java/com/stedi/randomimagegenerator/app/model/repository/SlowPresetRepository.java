package com.stedi.randomimagegenerator.app.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.Utils;

import java.util.List;

public class SlowPresetRepository implements PresetRepository {
    private final PresetRepository target;

    public SlowPresetRepository(PresetRepository target) {
        this.target = target;
    }

    @Override
    public boolean save(@NonNull Preset preset) throws Exception {
        Utils.sleep(3000);
        return target.save(preset);
    }

    @Override
    public boolean remove(int id) throws Exception {
        Utils.sleep(3000);
        return target.remove(id);
    }

    @Nullable
    @Override
    public Preset get(int id) throws Exception {
        Utils.sleep(3000);
        return target.get(id);
    }

    @NonNull
    @Override
    public List<Preset> getAll() throws Exception {
        Utils.sleep(3000);
        return target.getAll();
    }
}
