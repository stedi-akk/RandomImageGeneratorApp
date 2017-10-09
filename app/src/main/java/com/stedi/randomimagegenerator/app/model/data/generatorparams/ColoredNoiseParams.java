package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.generators.ColoredNoiseGenerator;
import com.stedi.randomimagegenerator.generators.Generator;

@DatabaseTable(tableName = "colored_noise_params")
public class ColoredNoiseParams extends GeneratorParams {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "orientation", canBeNull = false)
    private ColoredNoiseGenerator.Orientation orientation = ColoredNoiseGenerator.Orientation.RANDOM;
    @DatabaseField(columnName = "type", canBeNull = false)
    private ColoredNoiseGenerator.Type type = ColoredNoiseGenerator.Type.RANDOM;

    public ColoredNoiseParams() {
    }

    public void setNoiseOrientation(@NonNull ColoredNoiseGenerator.Orientation orientation) {
        this.orientation = orientation;
    }

    public void setNoiseType(@NonNull ColoredNoiseGenerator.Type type) {
        this.type = type;
    }

    @NonNull
    public ColoredNoiseGenerator.Orientation getNoiseOrientation() {
        return orientation;
    }

    @NonNull
    public ColoredNoiseGenerator.Type getNoiseType() {
        return type;
    }

    @NonNull
    @Override
    public Generator createGenerator() {
        return new ColoredNoiseGenerator(orientation, type);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @NonNull
    @Override
    public GeneratorType getType() {
        return GeneratorType.COLORED_NOISE;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;

        ColoredNoiseParams that = (ColoredNoiseParams) o;

        if (orientation != that.orientation) return false;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result = orientation.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.orientation == null ? -1 : this.orientation.ordinal());
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeInt(this.id);
    }

    protected ColoredNoiseParams(Parcel in) {
        int tmpOrientation = in.readInt();
        this.orientation = tmpOrientation == -1 ? null : ColoredNoiseGenerator.Orientation.values()[tmpOrientation];
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : ColoredNoiseGenerator.Type.values()[tmpType];
        this.id = in.readInt();
    }

    public static final Creator<ColoredNoiseParams> CREATOR = new Creator<ColoredNoiseParams>() {
        @Override
        public ColoredNoiseParams createFromParcel(Parcel source) {
            return new ColoredNoiseParams(source);
        }

        @Override
        public ColoredNoiseParams[] newArray(int size) {
            return new ColoredNoiseParams[size];
        }
    };
}
