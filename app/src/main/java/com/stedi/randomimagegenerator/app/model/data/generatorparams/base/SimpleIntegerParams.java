package com.stedi.randomimagegenerator.app.model.data.generatorparams.base;

import android.os.Parcel;

public abstract class SimpleIntegerParams extends GeneratorParams {
    private Integer value;

    public SimpleIntegerParams() {
        if (!canBeRandom())
            value = 1;
    }

    public Integer getValue() {
        return value;
    }

    public void setRandomValue() {
        if (canBeRandom())
            this.value = null;
    }

    public void setValue(int value) {
        if (value < 1)
            throw new IllegalArgumentException("value must be > 0");
        this.value = value;
    }

    public abstract boolean canBeRandom();

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;

        SimpleIntegerParams that = (SimpleIntegerParams) o;

        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.value);
    }

    protected SimpleIntegerParams(Parcel in) {
        this.value = (Integer) in.readValue(Integer.class.getClassLoader());
    }
}
