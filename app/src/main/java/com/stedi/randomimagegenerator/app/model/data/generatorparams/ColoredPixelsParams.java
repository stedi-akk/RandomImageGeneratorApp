package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.generators.ColoredPixelsGenerator;
import com.stedi.randomimagegenerator.generators.Generator;

public class ColoredPixelsParams extends GeneratorParams {
    private int pixelMultiplier = 1;

    public int getPixelMultipler() {
        return pixelMultiplier;
    }

    public void setPixelMultipler(int pixelMultiplier) {
        if (pixelMultiplier < 1)
            throw new IllegalArgumentException("pixelMultiplier must be > 0");
        this.pixelMultiplier = pixelMultiplier;
    }

    @NonNull
    @Override
    public Generator createGenerator() {
        return new ColoredPixelsGenerator(pixelMultiplier);
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @NonNull
    @Override
    public GeneratorType getType() {
        return GeneratorType.COLORED_PIXELS;
    }
}
