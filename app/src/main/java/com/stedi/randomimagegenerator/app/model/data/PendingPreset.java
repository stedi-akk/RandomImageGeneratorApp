package com.stedi.randomimagegenerator.app.model.data;

import android.support.annotation.NonNull;
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
    private Preset candidateFrom;
    private Preset presetCandidate;

    @Inject
    public PendingPreset(@NonNull @Named("DefaultSavePath") String defaultSavePath) {
        this.defaultSavePath = defaultSavePath;
    }

    @Nullable
    public Preset get() {
        return preset;
    }

    public void newDefaultCandidate() {
        candidateFrom = null;
        presetCandidate = new Preset(
                "Unsaved preset",
                new FlatColorParams(),
                Quality.png(),
                defaultSavePath);
        presetCandidate.setWidth(100);
        presetCandidate.setHeight(100);
        presetCandidate.setCount(1);
    }

    public void prepareCandidateFrom(@NonNull Preset candidate) {
        if (candidate == preset) {
            candidateFrom = null;
            presetCandidate = candidate;
        } else {
            candidateFrom = candidate;
            presetCandidate = candidate.createCopy();
        }
    }

    public void candidateSaved() {
        prepareCandidateFrom(getCandidate());
    }

    public Preset getCandidate() {
        return presetCandidate;
    }

    public boolean isCandidateNewOrChanged() {
        return presetCandidate != null && !presetCandidate.equals(candidateFrom);
    }

    public void applyCandidate() {
        preset = presetCandidate;
    }

    public void killCandidate() {
        candidateFrom = null;
        presetCandidate = null;
    }

    public void clear() {
        preset = null;
    }
}
