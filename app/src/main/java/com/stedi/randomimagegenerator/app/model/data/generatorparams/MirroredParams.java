package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.generators.Generator;
import com.stedi.randomimagegenerator.generators.MirroredGenerator;

public class MirroredParams extends EffectGeneratorParams {
    public MirroredParams(@NonNull GeneratorParams target) {
        super(target);
    }

    @NonNull
    @Override
    protected Generator createEffectGenerator(@NonNull Generator target) {
        return new MirroredGenerator(target);
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @NonNull
    @Override
    public GeneratorType getType() {
        return GeneratorType.MIRRORED;
    }
}
