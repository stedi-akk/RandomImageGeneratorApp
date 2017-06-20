package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.SimpleIntegerParams;
import com.stedi.randomimagegenerator.generators.ColoredPixelsGenerator;
import com.stedi.randomimagegenerator.generators.Generator;

public class ColoredPixelsParams extends SimpleIntegerParams {
    @NonNull
    @Override
    public Generator createGenerator() {
        return new ColoredPixelsGenerator(getValue());
    }

    @Override
    public boolean canBeRandom() {
        return false;
    }

    @NonNull
    @Override
    public GeneratorType getType() {
        return GeneratorType.COLORED_PIXELS;
    }
}
