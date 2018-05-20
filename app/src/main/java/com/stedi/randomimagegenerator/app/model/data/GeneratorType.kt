package com.stedi.randomimagegenerator.app.model.data

enum class GeneratorType(val isEffect: Boolean, val isEditable: Boolean) {
    RANDOM_NON_EFFECT(false, false),
    FLAT_COLOR(false, false),
    COLORED_RECTANGLE(false, true),
    COLORED_CIRCLES(false, true),
    COLORED_LINES(false, true),
    COLORED_PIXELS(false, true),
    COLORED_NOISE(false, true),
    MIRRORED(true, false),
    THRESHOLD(true, false),
    TEXT_OVERLAY(true, false);

    companion object {
        val EFFECT_TYPES: Array<GeneratorType> by lazy {
            GeneratorType.values().filter { it.isEffect }.toTypedArray()
        }

        val NON_EFFECT_TYPES: Array<GeneratorType> by lazy {
            GeneratorType.values().filter { !it.isEffect }.toTypedArray()
        }
    }
}

fun GeneratorType?.effectOrdinal() = GeneratorType.EFFECT_TYPES.indexOf(this)

fun GeneratorType?.nonEffectOrdinal() = GeneratorType.NON_EFFECT_TYPES.indexOf(this)