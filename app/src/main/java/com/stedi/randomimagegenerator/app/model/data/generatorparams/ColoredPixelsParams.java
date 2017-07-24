package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.SimpleIntegerParams;
import com.stedi.randomimagegenerator.generators.ColoredPixelsGenerator;
import com.stedi.randomimagegenerator.generators.Generator;

@DatabaseTable(tableName = "colored_pixels_params")
public class ColoredPixelsParams extends SimpleIntegerParams {
    @DatabaseField(generatedId = true)
    private int id;

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

    @Override
    public int getId() {
        return id;
    }

    @NonNull
    @Override
    public GeneratorType getType() {
        return GeneratorType.COLORED_PIXELS;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;

        ColoredPixelsParams that = (ColoredPixelsParams) o;

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

    protected ColoredPixelsParams(Parcel in) {
        super(in);
        this.id = in.readInt();
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
