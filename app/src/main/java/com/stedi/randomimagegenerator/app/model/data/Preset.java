package com.stedi.randomimagegenerator.app.model.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;

import java.util.Arrays;

public class Preset {
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
                ", name='" + name + '\'' +
                ", count=" + count +
                ", width=" + width +
                ", height=" + height +
                ", widthRange=" + Arrays.toString(widthRange) +
                ", heightRange=" + Arrays.toString(heightRange) +
                '}';
    }
}
