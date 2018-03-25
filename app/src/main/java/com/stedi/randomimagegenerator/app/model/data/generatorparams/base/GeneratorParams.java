package com.stedi.randomimagegenerator.app.model.data.generatorparams.base;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.ColoredCirclesParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.ColoredNoiseParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.ColoredPixelsParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.ColoredRectangleParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.FlatColorParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.MirroredParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.TextOverlayParams;
import com.stedi.randomimagegenerator.generators.Generator;

public abstract class GeneratorParams implements Parcelable {
    @NonNull
    public Generator getGenerator() {
        return GeneratorProxyKt.proxy(createGenerator());
    }

    public abstract void setId(int id);

    public abstract int getId();

    public abstract boolean isEditable();

    @NonNull
    public abstract GeneratorType getType();

    @NonNull
    public static GeneratorParams createDefaultParams(@NonNull GeneratorType type) {
        if (type.isEffect())
            throw new IllegalArgumentException("type must not be effect");

        switch (type) {
            case FLAT_COLOR:
                return new FlatColorParams();
            case COLORED_PIXELS:
                ColoredPixelsParams pixelsParams = new ColoredPixelsParams();
                pixelsParams.setValue(10);
                return pixelsParams;
            case COLORED_CIRCLES:
                ColoredCirclesParams circlesParams = new ColoredCirclesParams();
                circlesParams.setValue(50);
                return circlesParams;
            case COLORED_RECTANGLE:
                ColoredRectangleParams rectangleParams = new ColoredRectangleParams();
                rectangleParams.setValue(50);
                return rectangleParams;
            case COLORED_NOISE:
                return new ColoredNoiseParams();
            default:
                throw new IllegalStateException("unreachable code");
        }
    }

    @NonNull
    public static EffectGeneratorParams createDefaultEffectParams(@NonNull GeneratorType effectType, @NonNull GeneratorParams target) {
        if (!effectType.isEffect())
            throw new IllegalArgumentException("type must be effect");

        switch (effectType) {
            case MIRRORED:
                return new MirroredParams(target);
            case TEXT_OVERLAY:
                return new TextOverlayParams(target);
            default:
                throw new IllegalStateException("unreachable code");
        }
    }

    @NonNull
    protected abstract Generator createGenerator();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }

    @Override
    public String toString() {
        return "GeneratorParams{" +
                "getType()=" + getType() +
                ", isEditable()=" + isEditable() +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
