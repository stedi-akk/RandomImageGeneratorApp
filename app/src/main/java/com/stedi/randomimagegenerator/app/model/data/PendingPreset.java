package com.stedi.randomimagegenerator.app.model.data;

import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.FlatColorParams;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class PendingPreset {
    private final String defaultSavePath;

    private Preset preset;
    private Preset presetCandidate;

    @Inject
    public PendingPreset(@Named("DefaultSavePath") String defaultSavePath) {
        this.defaultSavePath = defaultSavePath;
    }

    @Nullable
    public Preset get() {
        return preset;
    }

    public void newCandidate() {
        presetCandidate = new Preset(
                "Unsaved preset",
                new FlatColorParams(),
                Quality.png(),
                defaultSavePath);
        presetCandidate.setWidth(100);
        presetCandidate.setHeight(100);
        presetCandidate.setCount(1);
    }

    public void setCandidate(Preset candidate) {
        presetCandidate = candidate;
    }

    public Preset getCandidate() {
        return presetCandidate;
    }

    public void applyCandidate() {
        preset = presetCandidate;
        presetCandidate = null;
    }

    public void clear() {
        preset = null;
        presetCandidate = null;
    }
}
