package com.stedi.randomimagegenerator.app.model.data.generatorparams

import android.os.Parcel
import android.os.Parcelable
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams
import com.stedi.randomimagegenerator.generators.Generator
import com.stedi.randomimagegenerator.generators.MirroredGenerator

@DatabaseTable(tableName = "mirrored_params")
class MirroredParams : EffectGeneratorParams {

    @DatabaseField(generatedId = true)
    private var id: Int = 0

    // OrmLite required
    constructor()

    constructor(target: GeneratorParams) : super(target)

    override fun createEffectGenerator(target: Generator) = MirroredGenerator(target)

    override fun setId(id: Int) {
        this.id = id
    }

    override fun getId() = id

    override fun getType() = GeneratorType.MIRRORED

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeInt(this.id)
    }

    protected constructor(parcel: Parcel) : super(parcel) {
        this.id = parcel.readInt()
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<MirroredParams> {
            override fun createFromParcel(source: Parcel) = MirroredParams(source)

            override fun newArray(size: Int) = arrayOfNulls<MirroredParams>(size)
        }
    }
}