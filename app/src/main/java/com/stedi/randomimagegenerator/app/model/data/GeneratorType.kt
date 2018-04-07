package com.stedi.randomimagegenerator.app.model.data

import com.stedi.randomimagegenerator.app.R

enum class GeneratorType(val isEffect: Boolean, val nameRes: Int) {
    COLORED_CIRCLES(false, R.string.generator_colored_circles),
    COLORED_RECTANGLE(false, R.string.generator_colored_rectangle),
    COLORED_PIXELS(false, R.string.generator_colored_pixels),
    FLAT_COLOR(false, R.string.generator_flat_color),
    COLORED_NOISE(false, R.string.generator_colored_noise),
    MIRRORED(true, R.string.effect_mirrored),
    TEXT_OVERLAY(true, R.string.effect_text_overlay);

    companion object {
        val EFFECT_TYPES: Array<GeneratorType> by lazy {
            GeneratorType.values().filter { it.isEffect }.toTypedArray()
        }

        val NON_EFFECT_TYPES: Array<GeneratorType> by lazy {
            GeneratorType.values().filter { !it.isEffect }.toTypedArray()
        }
    }
}