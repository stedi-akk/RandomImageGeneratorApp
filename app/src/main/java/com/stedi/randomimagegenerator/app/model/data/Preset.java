package com.stedi.randomimagegenerator.app.model.data;

import android.support.annotation.NonNull;

public class Preset {
    private final int id;
    private final String name;

    public Preset(int id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }
}
