package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.j256.ormlite.table.DatabaseTable;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.SimpleIntegerParams;
import com.stedi.randomimagegenerator.generators.ColoredRectangleGenerator;
import com.stedi.randomimagegenerator.generators.Generator;

@DatabaseTable(tableName = "colored_rectangle_params")
public class ColoredRectangleParams extends SimpleIntegerParams {
    public ColoredRectangleParams() {
        super();
    }

    @NonNull
    @Override
    public Generator createGenerator() {
        if (getValue() == null)
            return new ColoredRectangleGenerator();
        return new ColoredRectangleGenerator(getValue());
    }

    @Override
    public boolean canBeRandom() {
        return true;
    }

    @NonNull
    @Override
    public GeneratorType getType() {
        return GeneratorType.COLORED_RECTANGLE;
    }

    protected ColoredRectangleParams(Parcel in) {
        super(in);
    }

    public static final Creator<ColoredRectangleParams> CREATOR = new Creator<ColoredRectangleParams>() {
        @Override
        public ColoredRectangleParams createFromParcel(Parcel source) {
            return new ColoredRectangleParams(source);
        }

        @Override
        public ColoredRectangleParams[] newArray(int size) {
            return new ColoredRectangleParams[size];
        }
    };
}
