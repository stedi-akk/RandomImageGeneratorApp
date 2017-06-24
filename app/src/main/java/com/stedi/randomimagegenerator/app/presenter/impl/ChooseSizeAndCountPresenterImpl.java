package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSizeAndCountPresenter;

public class ChooseSizeAndCountPresenterImpl implements ChooseSizeAndCountPresenter {
    private final PendingPreset pendingPreset;
    private final Logger logger;

    private UIImpl ui;

    public ChooseSizeAndCountPresenterImpl(@NonNull PendingPreset pendingPreset, @NonNull Logger logger) {
        if (pendingPreset.getCandidate() == null)
            throw new IllegalStateException("pending preset candidate must not be null");
        this.pendingPreset = pendingPreset;
        this.logger = logger;
    }

    @Override
    public void getValues() {
        Preset preset = pendingPreset.getCandidate();
        boolean showCount = true;
        int[] widthRange = preset.getWidthRange();
        if (widthRange != null) {
            ui.showWidthRange(widthRange[0], widthRange[1], widthRange[2]);
            showCount = false;
        } else {
            ui.showWidth(preset.getWidth());
        }
        int[] heightRange = preset.getHeightRange();
        if (heightRange != null) {
            ui.showHeightRange(heightRange[0], heightRange[1], heightRange[2]);
            showCount = false;
        } else {
            ui.showHeight(preset.getHeight());
        }
        if (showCount)
            ui.showCount(preset.getCount());
    }

    @Override
    public void setCount(int count) {
        if (count < 1) {
            ui.onError(Error.INCORRECT_COUNT);
            return;
        }
        pendingPreset.getCandidate().setCount(count);
    }

    @Override
    public void setWidth(int width) {
        if (width < 1) {
            ui.onError(Error.INCORRECT_WIDTH);
            return;
        }
        pendingPreset.getCandidate().setWidth(width);
    }

    @Override
    public void setHeight(int height) {
        if (height < 1) {
            ui.onError(Error.INCORRECT_HEIGHT);
            return;
        }
        pendingPreset.getCandidate().setHeight(height);
    }

    @Override
    public void setWidthRange(int from, int to, int step) {
        if (from < 1 || to < 1 || step < 1) {
            ui.onError(Error.INCORRECT_WIDTH_RANGE);
            return;
        }
        pendingPreset.getCandidate().setWidthRange(from, to, step);
    }

    @Override
    public void setHeightRange(int from, int to, int step) {
        if (from < 1 || to < 1 || step < 1) {
            ui.onError(Error.INCORRECT_HEIGHT_RANGE);
            return;
        }
        pendingPreset.getCandidate().setHeightRange(from, to, step);
    }

    @Override
    public void onAttach(@NonNull UIImpl ui) {
        this.ui = ui;
    }

    @Override
    public void onDetach() {
        this.ui = null;
    }
}
