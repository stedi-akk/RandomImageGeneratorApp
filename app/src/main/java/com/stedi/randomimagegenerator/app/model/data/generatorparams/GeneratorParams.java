package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.generators.Generator;

public interface GeneratorParams {
    @NonNull
    Generator createGenerator();
}
