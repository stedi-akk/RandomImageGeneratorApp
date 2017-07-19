package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.j256.ormlite.table.DatabaseTable;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.generators.FlatColorGenerator;
import com.stedi.randomimagegenerator.generators.Generator;

@DatabaseTable(tableName = "flat_color_params")
public class FlatColorParams extends GeneratorParams {
    public FlatColorParams() {
    }

    @NonNull
    @Override
    public Generator createGenerator() {
        return new FlatColorGenerator();
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
    public void writeToParcel(Parcel dest, int flags) {
    }

    protected FlatColorParams(Parcel in) {
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
