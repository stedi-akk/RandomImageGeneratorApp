package com.stedi.randomimagegenerator.app.model.data.generatorparams.base

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.CallSuper
import com.j256.ormlite.field.DatabaseField
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.model.data.generatorparams.*
import com.stedi.randomimagegenerator.generators.Generator

@Suppress("EqualsOrHashCode")
abstract class GeneratorParams : Parcelable {

    @DatabaseField(generatedId = true)
    var id: Int = 0

    // OrmLite required
    constructor()

    companion object {
        fun createDefaultParams(type: GeneratorType): GeneratorParams {
            if (type.isEffect) {
                throw IllegalArgumentException("type must not be effect")
            }

            when (type) {
                GeneratorType.FLAT_COLOR -> return FlatColorParams()
                GeneratorType.COLORED_PIXELS -> {
                    val pixelsParams = ColoredPixelsParams()
                    pixelsParams.setValue(10)
                    return pixelsParams
                }
                GeneratorType.COLORED_CIRCLES -> {
                    val circlesParams = ColoredCirclesParams()
                    circlesParams.setValue(50)
                    return circlesParams
                }
                GeneratorType.COLORED_RECTANGLE -> {
                    val rectangleParams = ColoredRectangleParams()
                    rectangleParams.setValue(50)
                    return rectangleParams
                }
                GeneratorType.COLORED_NOISE -> return ColoredNoiseParams()
                else -> throw IllegalStateException("unreachable code")
            }
        }

        fun createDefaultEffectParams(effectType: GeneratorType, target: GeneratorParams): EffectGeneratorParams {
            if (!effectType.isEffect) {
                throw IllegalArgumentException("type must be effect")
            }

            when (effectType) {
                GeneratorType.MIRRORED -> return MirroredParams(target)
                GeneratorType.TEXT_OVERLAY -> return TextOverlayParams(target)
                else -> throw IllegalStateException("unreachable code")
            }
        }
    }

    fun getGenerator() = proxy(createGenerator())

    abstract fun getType(): GeneratorType

    protected abstract fun createGenerator(): Generator

    @CallSuper
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return !(other == null || javaClass != other.javaClass)
    }

    override fun toString() = "GeneratorParams{" +
            "getType()=${getType()}" +
            '}'

    @CallSuper
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(this.id)
    }

    protected constructor(parcel: Parcel) {
        this.id = parcel.readInt()
    }

    override fun describeContents() = 0
}