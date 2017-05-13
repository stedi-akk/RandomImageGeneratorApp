package com.stedi.randomimagegenerator.app.model.data;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.generatorparams.GeneratorParams;

public class Preset {
    private int id;
    private long timestamp;
    private String name;
    private GeneratorParams generatorParams;

    public Preset(@NonNull String name, @NonNull GeneratorParams generatorParams) {
        this.name = name;
        this.generatorParams = generatorParams;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setName(@NonNull String name) {
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
}
