package com.stedi.randomimagegenerator.app.model.data.generatorparams.base;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.generators.Generator;

public abstract class EffectGeneratorParams extends GeneratorParams {
    @DatabaseField(columnName = "target_generator_type", canBeNull = false)
    private GeneratorType targetGeneratorType;
    @DatabaseField(columnName = "target_generator_params_id")
    private int targetGeneratorParamsId;
    private GeneratorParams target;

    public EffectGeneratorParams() {
    }

    public EffectGeneratorParams(@NonNull GeneratorParams target) {
        setTarget(target);
    }

    public void setTarget(@NonNull GeneratorParams target) {
        this.target = target;
        this.targetGeneratorType = target.getType();
    }

    @NonNull
    public GeneratorParams getTarget() {
        return target;
    }

    public GeneratorType getTargetGeneratorType() {
        return targetGeneratorType;
    }

    public void setTargetGeneratorType(GeneratorType targetGeneratorType) {
        this.targetGeneratorType = targetGeneratorType;
    }

    public int getTargetGeneratorParamsId() {
        return targetGeneratorParamsId;
    }

    public void setTargetGeneratorParamsId(int targetGeneratorParamsId) {
        if (targetGeneratorParamsId < 1)
            throw new IllegalArgumentException("id must be > 0");
        this.targetGeneratorParamsId = targetGeneratorParamsId;
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

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;

        EffectGeneratorParams that = (EffectGeneratorParams) o;

        if (targetGeneratorParamsId != that.targetGeneratorParamsId) return false;
        return target.equals(that.target);
    }

    @Override
    public int hashCode() {
        int result = target.hashCode();
        return 31 * result + targetGeneratorParamsId;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.target, flags);
        dest.writeInt(this.targetGeneratorParamsId);
    }

    protected EffectGeneratorParams(Parcel in) {
        setTarget(in.readParcelable(GeneratorParams.class.getClassLoader()));
        this.targetGeneratorParamsId = in.readInt();
    }
}
