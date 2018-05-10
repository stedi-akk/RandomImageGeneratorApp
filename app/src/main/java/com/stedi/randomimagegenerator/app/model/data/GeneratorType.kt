package com.stedi.randomimagegenerator.app.model.data

enum class GeneratorType(val isEffect: Boolean, val isEditable: Boolean) {
    COLORED_CIRCLES(false, true),
    COLORED_RECTANGLE(false, true),
    COLORED_PIXELS(false, true),
    FLAT_COLOR(false, false),
    COLORED_NOISE(false, true),
    MIRRORED(true, false),
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