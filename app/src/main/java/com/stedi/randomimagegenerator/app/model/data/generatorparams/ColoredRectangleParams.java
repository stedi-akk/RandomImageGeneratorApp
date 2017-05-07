package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.generators.ColoredRectangleGenerator;
import com.stedi.randomimagegenerator.generators.Generator;

public class ColoredRectangleParams implements GeneratorParams {
    private Integer count;

    @Nullable
    public Integer getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @NonNull
    @Override
    public Generator createGenerator() {
        if (count == null)
            return new ColoredRectangleGenerator();
        return new ColoredRectangleGenerator(count);
    }
}
