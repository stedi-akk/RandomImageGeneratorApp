package com.stedi.randomimagegenerator.app.model.data;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.FlatColorParams;
import com.stedi.randomimagegenerator.app.other.logger.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class PendingPreset {
    private static final String KEY_MAIN_PRESET = "KEY_MAIN_PRESET";
    private static final String KEY_CANDIDATE_FROM_PRESET = "KEY_CANDIDATE_FROM_PRESET";
    private static final String KEY_CANDIDATE_PRESET = "KEY_CANDIDATE_PRESET";

    private final String defaultSavePath;
    private final Logger logger;

    private Preset preset;
    private Preset candidateFrom;
    private Preset candidate;

    @Inject
    public PendingPreset(@NonNull @Named("DefaultSavePath") String defaultSavePath, @NonNull Logger logger) {
        this.defaultSavePath = defaultSavePath;
        this.logger = logger;
    }

    @Nullable
    public Preset get() {
        return preset;
    }

    public void newDefaultCandidate() {
        candidateFrom = null;
        candidate = new Preset(
                "Unsaved preset",
                new FlatColorParams(),
                Quality.png(),
                defaultSavePath);
        candidate.setWidth(100);
        candidate.setHeight(100);
        candidate.setCount(1);
        logger.log(this, "after newDefaultCandidate:" + this);
    }

    public void prepareCandidateFrom(@NonNull Preset candidate) {
        if (candidate == preset) {
            this.candidateFrom = null;
            this.candidate = candidate;
        } else {
            this.candidateFrom = candidate;
            this.candidate = candidate.createCopy();
        }
        logger.log(this, "after prepareCandidateFrom:" + this);
    }

    public void candidateSaved() {
        prepareCandidateFrom(getCandidate());
    }

    public Preset getCandidate() {
        return candidate;
    }

    public boolean isCandidateNewOrChanged() {
        return candidate != null && !candidate.equals(candidateFrom);
    }

    public void applyCandidate() {
        preset = candidate;
        if (candidateFrom != null)
            preset.setName(String.format("Unsaved preset (from %s)", candidateFrom.getName()));
        preset.setTimestamp(System.currentTimeMillis());
        logger.log(this, "after applyCandidate:" + this);
    }

    public void killCandidate() {
        candidateFrom = null;
        candidate = null;
        logger.log(this, "after killCandidate:" + this);
    }

    public void clear() {
        preset = null;
        logger.log(this, "after clear:" + this);
    }

    public void retain(@NonNull Bundle bundle) {
        bundle.putParcelable(KEY_MAIN_PRESET, preset);
        bundle.putParcelable(KEY_CANDIDATE_FROM_PRESET, candidateFrom);
        bundle.putParcelable(KEY_CANDIDATE_PRESET, candidate);
        logger.log(this, "after retain: " + this);
    }

    public void restore(@NonNull Bundle bundle) {
        preset = bundle.getParcelable(KEY_MAIN_PRESET);
        candidateFrom = bundle.getParcelable(KEY_CANDIDATE_FROM_PRESET);
        candidate = bundle.getParcelable(KEY_CANDIDATE_PRESET);
        logger.log(this, "after restore: " + this);
    }

    @Override
    public String toString() {
        return "PendingPreset{" +
                "preset=" + preset +
                ", candidateFrom=" + candidateFrom +
                ", candidate=" + candidate +
                '}';
    }
}
