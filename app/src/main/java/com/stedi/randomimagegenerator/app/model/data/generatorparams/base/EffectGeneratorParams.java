package com.stedi.randomimagegenerator.app.model.data.generatorparams.base;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.stedi.randomimagegenerator.generators.Generator;

public abstract class EffectGeneratorParams extends GeneratorParams {
    @DatabaseField(columnName = "generator_params_target", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private GeneratorParams target;

    public EffectGeneratorParams() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;

        EffectGeneratorParams that = (EffectGeneratorParams) o;

        return target.equals(that.target);
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.target, flags);
    }

    protected EffectGeneratorParams(Parcel in) {
        this.target = in.readParcelable(GeneratorParams.class.getClassLoader());
    }
}
