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
        return new Preset("Unsaved preset", new FlatColorParams());
    }

    @Inject
    public PendingPreset() {
    }

    public boolean exists() {
        return preset != null;
    }

    public Preset get() {
        if (preset == null)
            throw new IllegalStateException("incorrect behavior");
        return preset;
    }

    public void set(Preset preset) {
        this.preset = preset;
    }
}
