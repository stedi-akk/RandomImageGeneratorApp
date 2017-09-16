package com.stedi.randomimagegenerator.app.model.data.generatorparams.base;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.SlowGenerator;
import com.stedi.randomimagegenerator.generators.Generator;

class GeneratorProxy {
    private GeneratorProxy() {
    }

    @NonNull
    static Generator proxy(@NonNull Generator generator) {
        return new SlowGenerator(generator);
    }
}
