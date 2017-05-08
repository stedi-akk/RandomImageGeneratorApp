package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.generators.ColoredNoiseGenerator;
import com.stedi.randomimagegenerator.generators.Generator;

public class ColoredNoiseParams extends GeneratorParams {
    private ColoredNoiseGenerator.Orientation orientation = ColoredNoiseGenerator.Orientation.RANDOM;
    private ColoredNoiseGenerator.Type type = ColoredNoiseGenerator.Type.RANDOM;

    public void setOrientationAndType(@NonNull ColoredNoiseGenerator.Orientation orientation,
                                      @NonNull ColoredNoiseGenerator.Type type) {
        this.orientation = orientation;
        this.type = type;
    }

    @NonNull
    public ColoredNoiseGenerator.Orientation getOrientation() {
        return orientation;
    }

    @NonNull
    public ColoredNoiseGenerator.Type getType() {
        return type;
    }

    @NonNull
    @Override
    public Generator createGenerator() {
        return new ColoredNoiseGenerator(orientation, type);
    }
}
