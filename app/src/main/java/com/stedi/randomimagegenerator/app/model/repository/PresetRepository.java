package com.stedi.randomimagegenerator.app.model.repository;

import com.stedi.randomimagegenerator.app.model.data.Preset;

import java.util.List;

public interface PresetRepository {
    boolean save(Preset preset);

    boolean remove(int id);

    Preset get(int id);

    List<Preset> getAll();
}
