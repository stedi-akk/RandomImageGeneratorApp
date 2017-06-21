package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.generators.ColoredNoiseGenerator;
import com.stedi.randomimagegenerator.generators.Generator;

public class ColoredNoiseParams extends GeneratorParams {
    private ColoredNoiseGenerator.Orientation orientation = ColoredNoiseGenerator.Orientation.RANDOM;
    private ColoredNoiseGenerator.Type type = ColoredNoiseGenerator.Type.RANDOM;

    public void setNoiseOrientation(@NonNull ColoredNoiseGenerator.Orientation orientation) {
        this.orientation = orientation;
    }

    public void setNoiseType(@NonNull ColoredNoiseGenerator.Type type) {
        this.type = type;
    }

    @NonNull
    public ColoredNoiseGenerator.Orientation getNoiseOrientation() {
        return orientation;
    }

    @NonNull
    public ColoredNoiseGenerator.Type getNoiseType() {
        return type;
    }

    @NonNull
    @Override
    public Generator createGenerator() {
        return new ColoredNoiseGenerator(orientation, type);
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @NonNull
    @Override
    public GeneratorType getType() {
        return GeneratorType.COLORED_NOISE;
    }
}
