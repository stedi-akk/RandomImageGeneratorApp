package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationPresenter;

import java.io.Serializable;

public class GenerationPresenterImpl implements GenerationPresenter {
    private final PendingPreset pendingPreset;
    private final Logger logger;

    private UIImpl ui;

    public GenerationPresenterImpl(PendingPreset pendingPreset, Logger logger) {
        this.pendingPreset = pendingPreset;
        this.logger = logger;
    }

    @Override
    public void onAttach(@NonNull UIImpl ui) {
        this.ui = ui;
        if (!pendingPreset.exists())
            pendingPreset.createNew();
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
