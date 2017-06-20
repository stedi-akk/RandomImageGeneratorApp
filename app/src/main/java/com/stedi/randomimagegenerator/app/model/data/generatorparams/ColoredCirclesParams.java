package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.SimpleIntegerParams;
import com.stedi.randomimagegenerator.generators.ColoredCirclesGenerator;
import com.stedi.randomimagegenerator.generators.Generator;

public class ColoredCirclesParams extends SimpleIntegerParams {
    @NonNull
    @Override
    public Generator createGenerator() {
        if (getValue() == null)
            return new ColoredCirclesGenerator();
        return new ColoredCirclesGenerator(getValue());
    }

    @Override
    public boolean canBeRandom() {
        return true;
    }

    @NonNull
    @Override
    public GeneratorType getType() {
        return GeneratorType.COLORED_CIRCLES;
    }
}
