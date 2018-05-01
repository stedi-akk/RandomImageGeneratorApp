package com.stedi.randomimagegenerator.app.model.data.generatorparams

import android.os.Parcel
import android.os.Parcelable
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams
import com.stedi.randomimagegenerator.generators.ColoredNoiseGenerator

@DatabaseTable(tableName = "colored_noise_params")
class ColoredNoiseParams : GeneratorParams {

    @DatabaseField(columnName = "orientation", canBeNull = false)
    var noiseOrientation: ColoredNoiseGenerator.Orientation = ColoredNoiseGenerator.Orientation.RANDOM

    @DatabaseField(columnName = "type", canBeNull = false)
    var noiseType: ColoredNoiseGenerator.Type = ColoredNoiseGenerator.Type.RANDOM

    // OrmLite required
    constructor()

    public override fun createGenerator() = ColoredNoiseGenerator(noiseOrientation, noiseType)

    override fun getType() = GeneratorType.COLORED_NOISE

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false
        val that = other as ColoredNoiseParams
        return if (noiseOrientation != that.noiseOrientation) false else noiseType == that.noiseType
    }

    override fun hashCode(): Int {
        var result = noiseOrientation.hashCode()
        result = 31 * result + noiseType.hashCode()
        return result
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeInt(this.noiseOrientation.ordinal)
        dest.writeInt(this.noiseType.ordinal)
    }

    protected constructor(parcel: Parcel) : super(parcel) {
        this.noiseOrientation = ColoredNoiseGenerator.Orientation.values()[parcel.readInt()]
        this.noiseType = ColoredNoiseGenerator.Type.values()[parcel.readInt()]
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<ColoredNoiseParams> {
            override fun createFromParcel(source: Parcel) = ColoredNoiseParams(source)

            override fun newArray(size: Int) = arrayOfNulls<ColoredNoiseParams>(size)
        }
    }
}