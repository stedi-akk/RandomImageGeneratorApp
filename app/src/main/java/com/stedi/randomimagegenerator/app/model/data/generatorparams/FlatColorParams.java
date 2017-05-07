package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.generators.FlatColorGenerator;
import com.stedi.randomimagegenerator.generators.Generator;

public class FlatColorParams implements GeneratorParams {
    @NonNull
    @Override
    public Generator createGenerator() {
        return new FlatColorGenerator();
    }
}
