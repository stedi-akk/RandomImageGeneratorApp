package com.stedi.randomimagegenerator.app.model.data.generatorparams.base;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.generators.Generator;

public abstract class EffectGeneratorParams extends GeneratorParams {
    private GeneratorParams target;

    public EffectGeneratorParams(@NonNull GeneratorParams target) {
        this.target = target;
    }

    public void setTarget(@NonNull GeneratorParams target) {
        this.target = target;
    }

    @NonNull
    public GeneratorParams getTarget() {
        return target;
    }

    @NonNull
    @Override
    public Generator createGenerator() {
        return createEffectGenerator(target.createGenerator());
    }

    @NonNull
    protected abstract Generator createEffectGenerator(@NonNull Generator target);

    @Override
    public String toString() {
        return "EffectGeneratorParams{" +
                "getType()=" + getType() +
                ", isEditable()=" + isEditable() +
                ", target=" + target +
                '}';
    }
}