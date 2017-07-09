package com.stedi.randomimagegenerator.app.model.data.generatorparams.base;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.generators.Generator;

class GetGenerator {
    private GetGenerator() {
    }

    @NonNull
    static Generator proxy(@NonNull Generator generator) {
        return generator;
    }
}
