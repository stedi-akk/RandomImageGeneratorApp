package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.j256.ormlite.table.DatabaseTable;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.SimpleIntegerParams;
import com.stedi.randomimagegenerator.generators.ColoredPixelsGenerator;
import com.stedi.randomimagegenerator.generators.Generator;

@DatabaseTable(tableName = "colored_pixels_params")
public class ColoredPixelsParams extends SimpleIntegerParams {
    public ColoredPixelsParams() {
        super();
    }

    @NonNull
    @Override
    public Generator createGenerator() {
        return new ColoredPixelsGenerator(getValue());
    }

    @Override
    public boolean canBeRandom() {
        return false;
    }

    @NonNull
    @Override
    public GeneratorType getType() {
        return GeneratorType.COLORED_PIXELS;
    }

    protected ColoredPixelsParams(Parcel in) {
        super(in);
    }

    public static final Creator<ColoredPixelsParams> CREATOR = new Creator<ColoredPixelsParams>() {
        @Override
        public ColoredPixelsParams createFromParcel(Parcel source) {
            return new ColoredPixelsParams(source);
        }

        @Override
        public ColoredPixelsParams[] newArray(int size) {
            return new ColoredPixelsParams[size];
        }
    };
}
