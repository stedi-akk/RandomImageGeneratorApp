package com.stedi.randomimagegenerator.app.model.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;

import java.util.Arrays;

public class Preset implements Parcelable {
    private int id;
    private long timestamp;

    private String name;
    private GeneratorParams generatorParams;
    private Quality quality;
    private String saveFolder;

    private int count = 1;
    private int width = 1;
    private int height = 1;
    private int[] widthRange;
    private int[] heightRange;

    public Preset(@NonNull String name,
                  @NonNull GeneratorParams generatorParams,
                  @NonNull Quality quality,
                  @NonNull String saveFolder) {
        if (name.isEmpty())
            throw new IllegalArgumentException("name must not be empty");
        if (saveFolder.isEmpty())
            throw new IllegalArgumentException("saveFolder must not be empty");
        this.name = name;
        this.generatorParams = generatorParams;
        this.quality = quality;
        this.saveFolder = saveFolder;
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
    }

    @NonNull
    public GeneratorParams getGeneratorParams() {
        return generatorParams;
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
    }

    @NonNull
    public Quality getQuality() {
        return quality;
    }

    public void setSaveFolder(@NonNull String saveFolder) {
        if (saveFolder.isEmpty())
            throw new IllegalArgumentException("saveFolder must not be empty");
        this.saveFolder = saveFolder;
    }

    @NonNull
    public String getSaveFolder() {
        return saveFolder;
    }

    @Override
    public String toString() {
        return "Preset{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", name='" + name + '\'' +
                ", generatorParams=" + generatorParams +
                ", quality=" + quality +
                ", saveFolder='" + saveFolder + '\'' +
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

        if (id != preset.id) return false;
        if (timestamp != preset.timestamp) return false;
        if (count != preset.count) return false;
        if (width != preset.width) return false;
        if (height != preset.height) return false;
        if (!name.equals(preset.name)) return false;
        if (!generatorParams.equals(preset.generatorParams)) return false;
        if (!quality.equals(preset.quality)) return false;
        if (!saveFolder.equals(preset.saveFolder)) return false;
        if (!Arrays.equals(widthRange, preset.widthRange)) return false;
        return Arrays.equals(heightRange, preset.heightRange);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + generatorParams.hashCode();
        result = 31 * result + quality.hashCode();
        result = 31 * result + saveFolder.hashCode();
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
        dest.writeLong(this.timestamp);
        dest.writeString(this.name);
        dest.writeParcelable(this.generatorParams, flags);
        dest.writeInt(this.quality.getFormat().ordinal());
        dest.writeInt(this.quality.getQualityValue());
        dest.writeString(this.saveFolder);
        dest.writeInt(this.count);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeIntArray(this.widthRange);
        dest.writeIntArray(this.heightRange);
    }

    protected Preset(Parcel in) {
        this.id = in.readInt();
        this.timestamp = in.readLong();
        this.name = in.readString();
        this.generatorParams = in.readParcelable(GeneratorParams.class.getClassLoader());
        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.values()[in.readInt()];
        int qualityValue = in.readInt();
        this.quality = new Quality(compressFormat, qualityValue);
        this.saveFolder = in.readString();
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
