package com.stedi.randomimagegenerator.app.model.data;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.generatorparams.FlatColorParams;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PendingPreset {
    private Preset preset;

    @NonNull
    public static Preset createDefault() {
        Preset preset = new Preset("Unsaved preset");
        preset.setGeneratorParams(new FlatColorParams());
        return preset;
    }

    @Inject
    public PendingPreset() {
    }

    public boolean exists() {
        return preset != null;
    }

    @NonNull
    public Preset get() {
        if (preset == null)
            preset = PendingPreset.createDefault();
        return preset;
    }

    public void set(Preset preset) {
        this.preset = preset;
    }
}
