package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.generators.FlatColorGenerator;
import com.stedi.randomimagegenerator.generators.Generator;

public class FlatColorParams extends GeneratorParams {
    @NonNull
    @Override
    public Generator createGenerator() {
        return new FlatColorGenerator();
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @NonNull
    @Override
    public GeneratorType getType() {
        return GeneratorType.FLAT_COLOR;
    }
}
