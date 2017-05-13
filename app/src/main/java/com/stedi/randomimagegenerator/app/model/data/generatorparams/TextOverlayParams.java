package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.generators.Generator;
import com.stedi.randomimagegenerator.generators.TextOverlayGenerator;

public class TextOverlayParams extends GeneratorParams {
    private final GeneratorParams target;

    public TextOverlayParams(@NonNull GeneratorParams target) {
        this.target = target;
    }

    @NonNull
    @Override
    public Generator createGenerator() {
        return new TextOverlayGenerator.Builder()
                .setGenerator(target.createGenerator())
                .build();
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @NonNull
    @Override
    public GeneratorType getType() {
        return GeneratorType.TEXT_OVERLAY;
    }
}
