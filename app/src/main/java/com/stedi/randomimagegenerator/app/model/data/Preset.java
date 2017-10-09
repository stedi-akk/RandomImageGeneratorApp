package com.stedi.randomimagegenerator.app.model.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;

import java.util.Arrays;

@DatabaseTable(tableName = "preset")
public class Preset implements Parcelable {
    @DatabaseField(columnName = "generator_type", canBeNull = false)
    private GeneratorType generatorType;
    @DatabaseField(columnName = "generator_params_id")
    private int generatorParamsId;
    @DatabaseField(columnName = "quality_format", canBeNull = false)
    private Bitmap.CompressFormat qualityFormat;
    @DatabaseField(columnName = "quality_value")
    private int qualityValue;

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "timestamp")
    private long timestamp;
    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;
    private GeneratorParams generatorParams;
    private Quality quality;
    @DatabaseField(columnName = "path_to_save", canBeNull = false)
    private String pathToSave;

    @DatabaseField(columnName = "count")
    private int count = 1;
    @DatabaseField(columnName = "width")
    private int width = 1;
    @DatabaseField(columnName = "height")
    private int height = 1;
    @DatabaseField(columnName = "width_range", dataType = DataType.SERIALIZABLE)
    private int[] widthRange;
    @DatabaseField(columnName = "height_range", dataType = DataType.SERIALIZABLE)
    private int[] heightRange;

    public Preset() {
    }

    public Preset(@NonNull String name,
                  @NonNull GeneratorParams generatorParams,
                  @NonNull Quality quality,
                  @NonNull String pathToSave) {
        setName(name);
        setGeneratorParams(generatorParams);
        setQuality(quality);
        setPathToSave(pathToSave);
    }

    public Preset createCopy() {
        Parcel parcel = null;
        try {
            parcel = Parcel.obtain();
            writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            return CREATOR.createFromParcel(parcel);
        } finally {
            if (parcel != null)
                parcel.recycle();
        }
    }

    public int getGeneratorParamsId() {
        return generatorParamsId;
    }

    public void setGeneratorParamsId(int generatorParamsId) {
        if (generatorParamsId < 1)
            throw new IllegalArgumentException("id must be > 0");
        this.generatorParamsId = generatorParamsId;
    }

    public void setId(int id) {
        if (id < 1)
            throw new IllegalArgumentException("id must be > 0");
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTimestamp(long timestamp) {
        if (timestamp < 1)
            throw new IllegalArgumentException("timestamp must be > 0");
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setName(@NonNull String name) {
        if (name.isEmpty())
            throw new IllegalArgumentException("name must not be empty");
        this.name = name;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setGeneratorParams(@NonNull GeneratorParams generatorParams) {
        this.generatorParams = generatorParams;
        this.generatorType = generatorParams.getType();
    }

    @NonNull
    public GeneratorParams getGeneratorParams() {
        return generatorParams;
    }

    public GeneratorType getGeneratorType() {
        return generatorType;
    }

    public void setWidth(int width) {
        if (width < 1)
            throw new IllegalArgumentException("width must be > 0");
        this.width = width;
        widthRange = null;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        if (height < 1)
            throw new IllegalArgumentException("height must be > 0");
        this.height = height;
        heightRange = null;
    }

    public int getHeight() {
        return height;
    }

    public void setWidthRange(int from, int to, int step) {
        if (step <= 0 || from <= 0 || to <= 0)
            throw new IllegalArgumentException("all width range args must be bigger than 0");
        widthRange = new int[]{from, to, step};
        width = 0;
        count = 0;
    }

    @Nullable
    public int[] getWidthRange() {
        return widthRange;
    }

    public void setHeightRange(int from, int to, int step) {
        if (step <= 0 || from <= 0 || to <= 0)
            throw new IllegalArgumentException("all height range args must be bigger than 0");
        heightRange = new int[]{from, to, step};
        height = 0;
        count = 0;
    }

    @Nullable
    public int[] getHeightRange() {
        return heightRange;
    }

    public void setCount(int count) {
        if (widthRange != null || heightRange != null)
            throw new IllegalStateException("count can not be set with size range");
        if (count < 1)
            throw new IllegalArgumentException("count must be > 0");
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setQuality(@NonNull Quality quality) {
        this.quality = quality;
        this.qualityFormat = quality.getFormat();
        this.qualityValue = quality.getQualityValue();
    }

    @NonNull
    public Quality getQuality() {
        if (quality == null)
            quality = new Quality(qualityFormat, qualityValue);
        return quality;
    }

    public void setPathToSave(@NonNull String pathToSave) {
        if (pathToSave.isEmpty())
            throw new IllegalArgumentException("pathToSave must not be empty");
        this.pathToSave = pathToSave;
    }

    @NonNull
    public String getPathToSave() {
        return pathToSave;
    }

    @Override
    public String toString() {
        return "Preset{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", name='" + name + '\'' +
                ", generatorParams=" + generatorParams +
                ", quality=" + getQuality() +
                ", pathToSave='" + pathToSave + '\'' +
                ", count=" + count +
                ", width=" + width +
                ", height=" + height +
                ", widthRange=" + Arrays.toString(widthRange) +
                ", heightRange=" + Arrays.toString(heightRange) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Preset preset = (Preset) o;

        if (timestamp != preset.timestamp) return false;
        if (count != preset.count) return false;
        if (width != preset.width) return false;
        if (height != preset.height) return false;
        if (!name.equals(preset.name)) return false;
        if (!generatorParams.equals(preset.generatorParams)) return false;
        if (getQuality().getFormat() != preset.getQuality().getFormat()) return false;
        if (getQuality().getQualityValue() != preset.getQuality().getQualityValue()) return false;
        if (!pathToSave.equals(preset.pathToSave)) return false;
        if (!Arrays.equals(widthRange, preset.widthRange)) return false;
        return Arrays.equals(heightRange, preset.heightRange);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + generatorParams.hashCode();
        result = 31 * result + getQuality().getFormat().ordinal();
        result = 31 * result + getQuality().getQualityValue();
        result = 31 * result + pathToSave.hashCode();
        result = 31 * result + count;
        result = 31 * result + width;
        result = 31 * result + height;
        result = 31 * result + Arrays.hashCode(widthRange);
        result = 31 * result + Arrays.hashCode(heightRange);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.generatorParamsId);
        dest.writeLong(this.timestamp);
        dest.writeString(this.name);
        dest.writeParcelable(this.generatorParams, flags);
        dest.writeInt(this.getQuality().getFormat().ordinal());
        dest.writeInt(this.getQuality().getQualityValue());
        dest.writeString(this.pathToSave);
        dest.writeInt(this.count);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeIntArray(this.widthRange);
        dest.writeIntArray(this.heightRange);
    }

    protected Preset(Parcel in) {
        this.id = in.readInt();
        this.generatorParamsId = in.readInt();
        this.timestamp = in.readLong();
        this.name = in.readString();
        setGeneratorParams(in.readParcelable(GeneratorParams.class.getClassLoader()));
        setQuality(new Quality(Bitmap.CompressFormat.values()[in.readInt()], in.readInt()));
        this.pathToSave = in.readString();
        this.count = in.readInt();
        this.width = in.readInt();
        this.height = in.readInt();
        this.widthRange = in.createIntArray();
        this.heightRange = in.createIntArray();
    }

    public static final Creator<Preset> CREATOR = new Creator<Preset>() {
        @Override
        public Preset createFromParcel(Parcel source) {
            return new Preset(source);
        }

        @Override
        public Preset[] newArray(int size) {
            return new Preset[size];
        }
    };
}
