package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSizeAndCountPresenter;

import java.io.Serializable;

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
    public void setCount(int count) {
        if (count < 1)
            ui.showErrorIncorrectCount();
        pendingPreset.getCandidate().setCount(count);
    }

    @Override
    public void setWidth(int width) {
        if (width < 1)
            ui.showErrorIncorrectSize();
        pendingPreset.getCandidate().setWidth(width);
    }

    @Override
    public void setHeight(int height) {
        if (height < 1)
            ui.showErrorIncorrectSize();
        pendingPreset.getCandidate().setHeight(height);
    }

    @Override
    public void onAttach(@NonNull UIImpl ui) {
        this.ui = ui;
        Preset preset = pendingPreset.getCandidate();
        this.ui.showCount(preset.getCount());
        this.ui.showSize(preset.getWidth(), preset.getHeight());
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
