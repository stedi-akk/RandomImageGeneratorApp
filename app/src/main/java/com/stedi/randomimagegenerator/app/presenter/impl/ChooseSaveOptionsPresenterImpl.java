package com.stedi.randomimagegenerator.app.presenter.impl;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSaveOptionsPresenter;

public class ChooseSaveOptionsPresenterImpl implements ChooseSaveOptionsPresenter {
    private final PendingPreset pendingPreset;
    private final Logger logger;

    private UIImpl ui;

    public ChooseSaveOptionsPresenterImpl(@NonNull PendingPreset pendingPreset, @NonNull Logger logger) {
        if (pendingPreset.getCandidate() == null)
            throw new IllegalStateException("pending preset candidate must not be null");
        this.pendingPreset = pendingPreset;
        this.logger = logger;
    }

    @Override
    public void onAttach(@NonNull UIImpl ui) {
        this.ui = ui;
    }

    @Override
    public void getData() {
        Quality currentQuality = pendingPreset.getCandidate().getQuality();
        ui.showQualityFormat(currentQuality.getFormat());
        ui.showQualityValue(currentQuality.getQualityValue());
        ui.showSaveFolder(pendingPreset.getCandidate().getSaveFolder());
    }

    @Override
    public void setQualityFormat(@NonNull Bitmap.CompressFormat format) {
        Quality prevQuality = pendingPreset.getCandidate().getQuality();
        pendingPreset.getCandidate().setQuality(new Quality(format, prevQuality.getQualityValue()));
        logger.log(this, "after setQualityFormat " + pendingPreset.getCandidate().getQuality());
    }

    @Override
    public void setQualityValue(int value) {
        if (value < 0 || value > 100) {
            ui.onIncorrectQualityValue();
            return;
        }
        Quality prevQuality = pendingPreset.getCandidate().getQuality();
        pendingPreset.getCandidate().setQuality(new Quality(prevQuality.getFormat(), value));
        logger.log(this, "after setQualityValue " + pendingPreset.getCandidate().getQuality());
    }

    @Override
    public void onDetach() {
        this.ui = null;
    }
}
