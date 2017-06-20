package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.SimpleIntegerParams;
import com.stedi.randomimagegenerator.generators.ColoredRectangleGenerator;
import com.stedi.randomimagegenerator.generators.Generator;

public class ColoredRectangleParams extends SimpleIntegerParams {
    @NonNull
    @Override
    public Generator createGenerator() {
        if (getValue() == null)
            return new ColoredRectangleGenerator();
        return new ColoredRectangleGenerator(getValue());
    }

    @Override
    public boolean canBeRandom() {
        return true;
    }

    @NonNull
    @Override
    public GeneratorType getType() {
        return GeneratorType.COLORED_RECTANGLE;
    }
}
