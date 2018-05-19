package com.stedi.randomimagegenerator.app.model.data.generatorparams

import android.os.Parcel
import android.os.Parcelable
import com.j256.ormlite.table.DatabaseTable
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.SimpleIntegerParams
import com.stedi.randomimagegenerator.generators.ColoredLinesGenerator
import com.stedi.randomimagegenerator.generators.Generator

@DatabaseTable(tableName = "colored_lines_params")
class ColoredLinesParams : SimpleIntegerParams {

    // OrmLite required
    constructor()

    public override fun createGenerator(): Generator {
        return getValue()?.let { ColoredLinesGenerator(it) } ?: ColoredLinesGenerator()
    }

    override fun canBeRandom() = true

    override fun getType() = GeneratorType.COLORED_LINES

    protected constructor(parcel: Parcel) : super(parcel)

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<ColoredLinesParams> {
            override fun createFromParcel(source: Parcel) = ColoredLinesParams(source)

            override fun newArray(size: Int) = arrayOfNulls<ColoredLinesParams>(size)
        }
    }
}