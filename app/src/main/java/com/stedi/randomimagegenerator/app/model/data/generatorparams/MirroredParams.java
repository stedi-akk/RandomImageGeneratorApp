package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.generators.Generator;
import com.stedi.randomimagegenerator.generators.MirroredGenerator;

@DatabaseTable(tableName = "mirrored_params")
public class MirroredParams extends EffectGeneratorParams {
    @DatabaseField(generatedId = true)
    private int id;

    public MirroredParams() {
    }

    public MirroredParams(@NonNull GeneratorParams target) {
        super(target);
    }

    @NonNull
    @Override
    protected Generator createEffectGenerator(@NonNull Generator target) {
        return new MirroredGenerator(target);
    }

    @Override
    public int getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;

        MirroredParams that = (MirroredParams) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.id);
    }

    protected MirroredParams(Parcel in) {
        super(in);
        this.id = in.readInt();
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
