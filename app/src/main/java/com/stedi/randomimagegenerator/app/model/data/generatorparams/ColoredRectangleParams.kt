package com.stedi.randomimagegenerator.app.model.data.generatorparams

import android.os.Parcel
import android.os.Parcelable
import com.j256.ormlite.table.DatabaseTable
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.SimpleIntegerParams
import com.stedi.randomimagegenerator.generators.ColoredRectangleGenerator
import com.stedi.randomimagegenerator.generators.Generator

@DatabaseTable(tableName = "colored_rectangle_params")
class ColoredRectangleParams : SimpleIntegerParams {

    // OrmLite required
    constructor()

    public override fun createGenerator(): Generator {
        return getValue()?.let { ColoredRectangleGenerator(it) } ?: ColoredRectangleGenerator()
    }

    override fun canBeRandom() = true

    override fun getType() = GeneratorType.COLORED_RECTANGLE

    protected constructor(parcel: Parcel) : super(parcel)

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<ColoredRectangleParams> {
            override fun createFromParcel(source: Parcel) = ColoredRectangleParams(source)

            override fun newArray(size: Int) = arrayOfNulls<ColoredRectangleParams>(size)
        }
    }
}