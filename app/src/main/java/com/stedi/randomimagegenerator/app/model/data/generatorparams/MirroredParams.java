package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.generators.Generator;
import com.stedi.randomimagegenerator.generators.MirroredGenerator;

public class MirroredParams extends GeneratorParams {
    private final GeneratorParams target;

    public MirroredParams(@NonNull GeneratorParams target) {
        this.target = target;
    }

    @NonNull
    @Override
    public Generator createGenerator() {
        return new MirroredGenerator(target.createGenerator());
    }
}
