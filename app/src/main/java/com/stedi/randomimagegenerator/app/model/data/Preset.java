package com.stedi.randomimagegenerator.app.model.data;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.generatorparams.GeneratorParams;

public class Preset {
    private int id;
    private String name;
    private GeneratorParams generatorParams;

    public Preset(@NonNull String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGeneratorParams(@NonNull GeneratorParams generatorParams) {
        this.generatorParams = generatorParams;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public GeneratorParams getGeneratorParams() {
        return generatorParams;
    }
}
