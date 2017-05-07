package com.stedi.randomimagegenerator.app.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.ColoredCirclesParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.ColoredNoiseParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.ColoredPixelsParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.ColoredRectangleParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.FlatColorParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.logger.Logger;

import java.io.Serializable;

public class ChooseGeneratorPresenterImpl implements ChooseGeneratorPresenter {
    private final PendingPreset pendingPreset;
    private final Logger logger;

    private UIImpl ui;

    public ChooseGeneratorPresenterImpl(PendingPreset pendingPreset, Logger logger) {
        this.pendingPreset = pendingPreset;
        this.logger = logger;
    }

    @Override
    public void getGeneratorTypes() {
        ui.showTypesToChoose(Type.values());
    }

    @Override
    public void chooseGeneratorType(@NonNull Type type) {
        if (pendingPreset.get() == null)
            pendingPreset.set(PendingPreset.createDefault());
        pendingPreset.get().setGeneratorParams(createGeneratorParamsFromType(type));
        ui.onGeneratorTypeChose(type);
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

    private GeneratorParams createGeneratorParamsFromType(Type type) {
        switch (type) {
            case FLAT_COLOR:
                return new FlatColorParams();
            case COLORED_PIXELS:
                return new ColoredPixelsParams();
            case COLORED_CIRCLES:
                return new ColoredCirclesParams();
            case COLORED_RECTANGLE:
                return new ColoredRectangleParams();
            case COLORED_NOISE:
                return new ColoredNoiseParams();
            default:
                throw new IllegalStateException();
        }
    }
}
