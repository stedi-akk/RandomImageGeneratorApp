package com.stedi.randomimagegenerator.app.model.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

    @Nullable
    public Preset get() {
        return preset;
    }

    public void set(Preset preset) {
        this.preset = preset;
    }
}
