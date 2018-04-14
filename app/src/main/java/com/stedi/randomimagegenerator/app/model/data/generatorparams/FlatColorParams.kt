package com.stedi.randomimagegenerator.app.model.data.generatorparams

import android.os.Parcel
import android.os.Parcelable
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams
import com.stedi.randomimagegenerator.generators.FlatColorGenerator

@DatabaseTable(tableName = "flat_color_params")
class FlatColorParams : GeneratorParams {

    @DatabaseField(generatedId = true)
    private var id: Int = 0

    // OrmLite required
    constructor()

    public override fun createGenerator() = FlatColorGenerator()

    override fun setId(id: Int) {
        this.id = id
    }

    override fun getId() = id

    override fun getType() = GeneratorType.FLAT_COLOR

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(this.id)
    }

    protected constructor(parcel: Parcel) {
        this.id = parcel.readInt()
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<FlatColorParams> {
            override fun createFromParcel(source: Parcel) = FlatColorParams(source)

            override fun newArray(size: Int) = arrayOfNulls<FlatColorParams>(size)
        }
    }
}