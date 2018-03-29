package com.stedi.randomimagegenerator.app.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.ExtKt;

import java.util.List;
import java.util.Random;

public class FakePresetRepository implements PresetRepository {
    private final SparseArray<Preset> items = new SparseArray<>();

    private int latestId;

    public FakePresetRepository(int initialCount) {
        if (initialCount > 0) {
            Random random = new Random();
            for (int id = 1; id <= initialCount; id++) {
                GeneratorType gt = GeneratorType.values()[random.nextInt(GeneratorType.values().length)];
                GeneratorParams generatorParams;
                if (gt.isEffect()) {
                    generatorParams = GeneratorParams.Companion.createDefaultEffectParams(gt, GeneratorParams.Companion.createDefaultParams(GeneratorType.FLAT_COLOR));
                } else {
                    generatorParams = GeneratorParams.Companion.createDefaultParams(gt);
                }
                items.put(id, createTestPreset(id, generatorParams));
            }
            latestId = initialCount;
        }
    }

    @Override
    public void save(@NonNull Preset preset) throws Exception {
        if (preset.getId() == 0) {
            latestId++;
            preset.setId(latestId);
            items.put(latestId, preset);
        } else {
            items.put(preset.getId(), preset);
        }
    }

    @Override
    public void remove(int id) throws Exception {
        items.remove(id);
    }

    @Nullable
    @Override
    public Preset get(int id) throws Exception {
        return items.get(id);
    }

    @NonNull
    @Override
    public List<Preset> getAll() throws Exception {
        return ExtKt.toList(items);
    }

    private Preset createTestPreset(int id, GeneratorParams generatorParams) {
        Preset preset = TestUtils.newSimplePreset();
        preset.setName("test" + id);
        preset.setGeneratorParams(generatorParams);
        preset.setId(id);
        return preset;
    }
}