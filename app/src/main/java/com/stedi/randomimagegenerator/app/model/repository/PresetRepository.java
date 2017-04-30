package com.stedi.randomimagegenerator.app.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.model.data.Preset;

import java.util.List;

public interface PresetRepository {
    boolean save(@NonNull Preset preset) throws Exception;

    boolean remove(int id) throws Exception;

    @Nullable
    Preset get(int id) throws Exception;

    @NonNull
    List<Preset> getAll() throws Exception;
}
