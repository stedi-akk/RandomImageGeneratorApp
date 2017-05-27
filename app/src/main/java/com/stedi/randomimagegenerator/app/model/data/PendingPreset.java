package com.stedi.randomimagegenerator.app.model.data;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.FlatColorParams;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class PendingPreset {
    private final String defaultSavePath;

    private Preset preset;

    @Inject
    public PendingPreset(@Named("DefaultSavePath") String defaultSavePath) {
        this.defaultSavePath = defaultSavePath;
    }

    public void createNew() {
        preset = new Preset(
                "Unsaved preset",
                new FlatColorParams(),
                Quality.png(),
                defaultSavePath);
        preset.setWidth(100);
        preset.setHeight(100);
        preset.setCount(1);
    }

    public boolean exists() {
        return preset != null;
    }

    public Preset get() {
        if (preset == null)
            throw new IllegalStateException("incorrect behavior");
        return preset;
    }

    public void clear() {
        preset = null;
    }
}
