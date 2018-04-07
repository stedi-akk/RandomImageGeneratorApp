package com.stedi.randomimagegenerator.app.model.data.generatorparams

import android.os.Parcel
import android.os.Parcelable
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.SimpleIntegerParams
import com.stedi.randomimagegenerator.generators.ColoredCirclesGenerator
import com.stedi.randomimagegenerator.generators.Generator

@DatabaseTable(tableName = "colored_circles_params")
class ColoredCirclesParams : SimpleIntegerParams {

    @DatabaseField(generatedId = true)
    private var id: Int = 0

    // OrmLite required
    constructor()

    public override fun createGenerator(): Generator {
        return getValue()?.let { ColoredCirclesGenerator(it) } ?: ColoredCirclesGenerator()
    }

    override fun setId(id: Int) {
        this.id = id
    }

    override fun getId() = id

    override fun canBeRandom() = true

    override fun getType() = GeneratorType.COLORED_CIRCLES

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeInt(this.id)
    }

    protected constructor(parcel: Parcel) : super(parcel) {
        this.id = parcel.readInt()
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<ColoredCirclesParams> {
            override fun createFromParcel(source: Parcel) = ColoredCirclesParams(source)

            override fun newArray(size: Int) = arrayOfNulls<ColoredCirclesParams>(size)
        }
    }
}