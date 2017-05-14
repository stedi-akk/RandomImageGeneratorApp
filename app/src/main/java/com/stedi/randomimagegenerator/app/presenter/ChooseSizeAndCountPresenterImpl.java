package com.stedi.randomimagegenerator.app.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.logger.Logger;

import java.io.Serializable;

public class ChooseSizeAndCountPresenterImpl implements ChooseSizeAndCountPresenter {
    private final PendingPreset pendingPreset;
    private final Logger logger;

    private UIImpl ui;

    public ChooseSizeAndCountPresenterImpl(@NonNull PendingPreset pendingPreset, @NonNull Logger logger) {
        this.pendingPreset = pendingPreset;
        this.logger = logger;
    }

    @Override
    public void setCount(int count) {
        if (count < 1)
            ui.showErrorIncorrectCount();
        pendingPreset.get().setCount(count);
    }

    @Override
    public void setWidth(int width) {
        if (width < 1)
            ui.showErrorIncorrectSize();
        pendingPreset.get().setWidth(width);
    }

    @Override
    public void setHeight(int height) {
        if (height < 1)
            ui.showErrorIncorrectSize();
        pendingPreset.get().setHeight(height);
    }

    @Override
    public void onAttach(@NonNull UIImpl ui) {
        this.ui = ui;
        Preset preset = pendingPreset.get();
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
