package com.stedi.randomimagegenerator.app.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.otto.Bus;
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository;
import com.stedi.randomimagegenerator.app.other.logger.Logger;

import java.io.Serializable;

import rx.Scheduler;

public class HomePresenterImpl implements HomePresenter {
    private final PresetRepository presetRepository;
    private final Scheduler scheduler;
    private final Bus bus;
    private final Logger logger;

    private UIImpl ui;

    public HomePresenterImpl(PresetRepository presetRepository, Scheduler scheduler, Bus bus, Logger logger) {
        this.presetRepository = presetRepository;
        this.scheduler = scheduler;
        this.bus = bus;
        this.logger = logger;
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
