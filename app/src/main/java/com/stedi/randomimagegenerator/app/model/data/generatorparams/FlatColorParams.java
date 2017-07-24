package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.generators.FlatColorGenerator;
import com.stedi.randomimagegenerator.generators.Generator;

@DatabaseTable(tableName = "flat_color_params")
public class FlatColorParams extends GeneratorParams {
    @DatabaseField(generatedId = true)
    private int id;

    public FlatColorParams() {
    }

    @NonNull
    @Override
    public Generator createGenerator() {
        return new FlatColorGenerator();
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
        return GeneratorType.FLAT_COLOR;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;

        FlatColorParams that = (FlatColorParams) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
    }

    protected FlatColorParams(Parcel in) {
        this.id = in.readInt();
    }

    public static final Creator<FlatColorParams> CREATOR = new Creator<FlatColorParams>() {
        @Override
        public FlatColorParams createFromParcel(Parcel source) {
            return new FlatColorParams(source);
        }

        @Override
        public FlatColorParams[] newArray(int size) {
            return new FlatColorParams[size];
        }
    };
}
