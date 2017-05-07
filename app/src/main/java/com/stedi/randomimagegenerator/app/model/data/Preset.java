package com.stedi.randomimagegenerator.app.model.data;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.generators.Generator;

public class Preset {
    private int id;
    private String name;
    private Generator generator;

    public Preset(@NonNull String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGenerator(@NonNull Generator generator) {
        this.generator = generator;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public Generator getGenerator() {
        return generator;
    }
}
