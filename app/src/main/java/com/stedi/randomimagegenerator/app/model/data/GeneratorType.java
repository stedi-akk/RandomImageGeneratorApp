package com.stedi.randomimagegenerator.app.model.data;

public enum GeneratorType {
    FLAT_COLOR(false),
    COLORED_PIXELS(false),
    COLORED_CIRCLES(false),
    COLORED_RECTANGLE(false),
    COLORED_NOISE(false),
    MIRRORED(true),
    TEXT_OVERLAY(true);

    private final boolean isEffect;

    GeneratorType(boolean isEffect) {
        this.isEffect = isEffect;
    }

    public boolean isEffect() {
        return isEffect;
    }
}

