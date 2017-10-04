package com.stedi.randomimagegenerator.app.model.data;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.stedi.randomimagegenerator.app.R;

import java.util.ArrayList;
import java.util.List;

public enum GeneratorType {
    COLORED_CIRCLES(false, R.string.generator_colored_circles),
    COLORED_RECTANGLE(false, R.string.generator_colored_rectangle),
    COLORED_PIXELS(false, R.string.generator_colored_pixels),
    FLAT_COLOR(false, R.string.generator_flat_color),
    COLORED_NOISE(false, R.string.generator_colored_noise),
    MIRRORED(true, R.string.effect_mirrored),
    TEXT_OVERLAY(true, R.string.effect_text_overlay);

    private final boolean isEffect;
    private final int stringRes;

    private static GeneratorType[] effectTypes;
    private static GeneratorType[] nonEffectTypes;

    GeneratorType(boolean isEffect, int stringRes) {
        this.isEffect = isEffect;
        this.stringRes = stringRes;
    }

    public boolean isEffect() {
        return isEffect;
    }

    @StringRes
    public int getStringRes() {
        return stringRes;
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

