package com.stedi.randomimagegenerator.app.model.data;

public class Preset {
    private final int id;
    private final String name;

    public Preset(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }
}
