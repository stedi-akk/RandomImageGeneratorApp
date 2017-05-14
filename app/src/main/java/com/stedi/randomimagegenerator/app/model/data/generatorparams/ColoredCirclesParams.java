package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.generators.ColoredCirclesGenerator;
import com.stedi.randomimagegenerator.generators.Generator;

public class ColoredCirclesParams extends GeneratorParams {
    private Integer count;

    @Nullable
    public Integer getCount() {
        return count;
    }

    public void setRandomCount() {
        this.count = null;
    }

    public void setCount(int count) {
        if (count < 1)
            throw new IllegalArgumentException("count must be > 0");
        this.count = count;
    }

    @NonNull
    @Override
    public Generator createGenerator() {
        if (count == null)
            return new ColoredCirclesGenerator();
        return new ColoredCirclesGenerator(count);
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @NonNull
    @Override
    public GeneratorType getType() {
        return GeneratorType.COLORED_CIRCLES;
    }
}
