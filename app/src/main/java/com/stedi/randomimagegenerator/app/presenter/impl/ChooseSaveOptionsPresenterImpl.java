package com.stedi.randomimagegenerator.app.presenter.impl;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSaveOptionsPresenter;

import java.io.Serializable;

public class ChooseSaveOptionsPresenterImpl implements ChooseSaveOptionsPresenter {
    private final PendingPreset pendingPreset;
    private final Logger logger;

    private UIImpl ui;

    public ChooseSaveOptionsPresenterImpl(PendingPreset pendingPreset, Logger logger) {
        if (pendingPreset.getCandidate() == null)
            throw new IllegalStateException("pending preset candidate must not be null");
        this.pendingPreset = pendingPreset;
        this.logger = logger;
    }

    @Override
    public void chooseQualityFormat(@NonNull Bitmap.CompressFormat format) {

    }

    @Override
    public void chooseQualityValue(int value) {

    }

    @Override
    public void chooseSaveFolder(String path) {

    }

    @Override
    public void getFormatsToChoose() {
        ui.showQualityFormats(Bitmap.CompressFormat.values());
    }

    @Override
    public void getValuesToChoose() {
        ui.showQualityValueRange(new int[]{0, 100});
    }

    @Override
    public void getSaveFolder() {

    }

    @Override
    public void onAttach(@NonNull UIImpl ui) {
        this.ui = ui;
    }

    @Override
    public void onDetach() {
        this.ui = null;
    }

    @Override
    public void onRestore(@NonNull Serializable state) {

    }

    @Nullable
    @Override
    public Serializable onRetain() {
        return null;
    }
}
