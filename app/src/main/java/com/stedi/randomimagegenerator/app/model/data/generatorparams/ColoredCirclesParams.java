package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.SimpleIntegerParams;
import com.stedi.randomimagegenerator.generators.ColoredCirclesGenerator;
import com.stedi.randomimagegenerator.generators.Generator;

@DatabaseTable(tableName = "colored_circles_params")
public class ColoredCirclesParams extends SimpleIntegerParams {
    @DatabaseField(generatedId = true)
    private int id;

    public ColoredCirclesParams() {
        super();
    }

    @NonNull
    @Override
    public Generator createGenerator() {
        if (getValue() == null)
            return new ColoredCirclesGenerator();
        return new ColoredCirclesGenerator(getValue());
    }

    @Override
    public boolean canBeRandom() {
        return true;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @NonNull
    @Override
    public GeneratorType getType() {
        return GeneratorType.COLORED_CIRCLES;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.id);
    }

    protected ColoredCirclesParams(Parcel in) {
        super(in);
        this.id = in.readInt();
    }

    public static final Creator<ColoredCirclesParams> CREATOR = new Creator<ColoredCirclesParams>() {
        @Override
        public ColoredCirclesParams createFromParcel(Parcel source) {
            return new ColoredCirclesParams(source);
        }

        @Override
        public ColoredCirclesParams[] newArray(int size) {
            return new ColoredCirclesParams[size];
        }
    };
}
