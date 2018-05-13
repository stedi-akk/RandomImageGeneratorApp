package com.stedi.randomimagegenerator.app.model.data.generatorparams

import android.os.Parcel
import android.os.Parcelable
import com.j256.ormlite.table.DatabaseTable
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.SimpleIntegerParams
import com.stedi.randomimagegenerator.generators.ColoredPixelsGenerator
import com.stedi.randomimagegenerator.generators.Generator

@DatabaseTable(tableName = "colored_pixels_params")
class ColoredPixelsParams : SimpleIntegerParams {

    // OrmLite required
    constructor()

    public override fun createGenerator(): Generator {
        return getValue()?.let { ColoredPixelsGenerator(it) } ?: ColoredPixelsGenerator()
    }

    override fun canBeRandom() = false

    override fun getType() = GeneratorType.COLORED_PIXELS

    protected constructor(parcel: Parcel) : super(parcel)

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<ColoredPixelsParams> {
            override fun createFromParcel(source: Parcel) = ColoredPixelsParams(source)

            override fun newArray(size: Int) = arrayOfNulls<ColoredPixelsParams>(size)
        }
    }
}