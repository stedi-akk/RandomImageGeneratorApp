package com.stedi.randomimagegenerator.app.model.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.generators.FlatColorGenerator;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PendingPreset {
    private Preset preset;

    @NonNull
    public static Preset createDefault() {
        Preset preset = new Preset("Unsaved preset");
        preset.setGenerator(new FlatColorGenerator());
        return preset;
    }

    @Inject
    public PendingPreset() {
    }

    @Nullable
    public Preset getPreset() {
        return preset;
    }

    public void setPreset(Preset preset) {
        this.preset = preset;
    }
}
