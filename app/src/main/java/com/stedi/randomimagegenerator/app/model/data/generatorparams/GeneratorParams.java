package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorEffectType;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.generators.Generator;

public abstract class GeneratorParams {
    @NonNull
    abstract Generator createGenerator();

    public static GeneratorParams createDefaultFromType(GeneratorType type) {
        switch (type) {
            case FLAT_COLOR:
                return new FlatColorParams();
            case COLORED_PIXELS:
                return new ColoredPixelsParams();
            case COLORED_CIRCLES:
                return new ColoredCirclesParams();
            case COLORED_RECTANGLE:
                return new ColoredRectangleParams();
            case COLORED_NOISE:
                return new ColoredNoiseParams();
            default:
                throw new IllegalStateException("unreachable code");
        }
    }

    public static GeneratorParams createDefaultFromType(GeneratorEffectType type, GeneratorParams target) {
        switch (type) {
            case MIRRORED:
                return new MirroredParams(target);
            case TEXT_OVERLAY:
                return new TextOverlayParams(target);
            default:
                throw new IllegalStateException("unreachable code");
        }
    }
}
