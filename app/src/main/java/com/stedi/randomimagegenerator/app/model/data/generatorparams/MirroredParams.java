package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
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

    protected MirroredParams(Parcel in) {
        super(in);
    }

    public static final Creator<MirroredParams> CREATOR = new Creator<MirroredParams>() {
        @Override
        public MirroredParams createFromParcel(Parcel source) {
            return new MirroredParams(source);
        }

        @Override
        public MirroredParams[] newArray(int size) {
            return new MirroredParams[size];
        }
    };
}
