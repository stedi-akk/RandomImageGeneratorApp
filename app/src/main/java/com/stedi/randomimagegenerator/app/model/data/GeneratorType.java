package com.stedi.randomimagegenerator.app.model.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public enum GeneratorType {
    FLAT_COLOR(false),
    COLORED_PIXELS(false),
    COLORED_CIRCLES(false),
    COLORED_RECTANGLE(false),
    COLORED_NOISE(false),
    MIRRORED(true),
    TEXT_OVERLAY(true);

    private final boolean isEffect;

    private static GeneratorType[] effectTypes;
    private static GeneratorType[] nonEffectTypes;

    GeneratorType(boolean isEffect) {
        this.isEffect = isEffect;
    }

    public boolean isEffect() {
        return isEffect;
    }

    @NonNull
    public static GeneratorType[] nonEffectTypes() {
        if (nonEffectTypes == null) {
            List<GeneratorType> list = new ArrayList<>();
            for (GeneratorType type : GeneratorType.values()) {
                if (!type.isEffect())
                    list.add(type);
            }
            nonEffectTypes = list.toArray(new GeneratorType[list.size()]);
        }
        return nonEffectTypes;
    }

    @NonNull
    public static GeneratorType[] effectTypes() {
        if (effectTypes == null) {
            List<GeneratorType> list = new ArrayList<>();
            for (GeneratorType type : GeneratorType.values()) {
                if (type.isEffect())
                    list.add(type);
            }
            effectTypes = list.toArray(new GeneratorType[list.size()]);
        }
        return effectTypes;
    }
}

