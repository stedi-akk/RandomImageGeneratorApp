package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.generators.ColoredPixelsGenerator;
import com.stedi.randomimagegenerator.generators.Generator;

public class ColoredPixelsParams implements GeneratorParams {
    private int pixelMultiplier = 1;

    public int getPixelMultipler() {
        return pixelMultiplier;
    }

    public void setPixelMultipler(int pixelMultiplier) {
        this.pixelMultiplier = pixelMultiplier;
    }

    @NonNull
    @Override
    public Generator createGenerator() {
        return new ColoredPixelsGenerator(pixelMultiplier);
    }
}
