package com.stedi.randomimagegenerator.app.model.data;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.FlatColorParams;
import com.stedi.randomimagegenerator.app.other.logger.Logger;

public class PendingPreset {
    private static final String KEY_MAIN_PRESET = "KEY_MAIN_PRESET";
    private static final String KEY_CANDIDATE_FROM_PRESET = "KEY_CANDIDATE_FROM_PRESET";
    private static final String KEY_CANDIDATE_PRESET = "KEY_CANDIDATE_PRESET";

    private final String unsavedName;
    private final String pathToSave;
    private final Logger logger;

    private Preset preset;
    private Preset candidateFrom;
    private Preset candidate;

    public PendingPreset(@NonNull String unsavedName, @NonNull String pathToSave, @NonNull Logger logger) {
        this.unsavedName = unsavedName;
        this.pathToSave = pathToSave;
        this.logger = logger;
    }

    public Preset get() {
        return preset;
    }

    public void newDefaultCandidate() {
        candidateFrom = null;
        candidate = new Preset(
                unsavedName,
                new FlatColorParams(),
                Quality.png(),
                pathToSave);
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
        if (candidate == null)
            throw new IllegalStateException("candidate is null");
        prepareCandidateFrom(candidate);
    }

    public Preset getCandidate() {
        return candidate;
    }

    public boolean isCandidateNewOrChanged() {
        if (candidate == null)
            throw new IllegalStateException("candidate is null");
        return !candidate.equals(candidateFrom);
    }

    public void applyCandidate() {
        if (candidate == null)
            throw new IllegalStateException("candidate is null");
        preset = candidate;
        if (candidateFrom != null)
            preset.setName(String.format(unsavedName + " (%s)", candidateFrom.getName()));
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
