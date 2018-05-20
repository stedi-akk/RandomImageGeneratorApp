package com.stedi.randomimagegenerator.app.model.data.generatorparams

import android.os.Parcel
import android.os.Parcelable
import com.j256.ormlite.table.DatabaseTable
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams
import com.stedi.randomimagegenerator.generators.Generator
import com.stedi.randomimagegenerator.generators.GeneratorPack

@DatabaseTable(tableName = "random_params")
class RandomParams : GeneratorParams {

    // OrmLite required
    constructor()

    override fun createGenerator(): Generator {
        return GeneratorPack(GeneratorType.NON_EFFECT_TYPES.filter { it != GeneratorType.RANDOM_NON_EFFECT }
                .map { GeneratorParams.createDefaultParams(it).getGenerator() })
    }

    override fun getType() = GeneratorType.RANDOM_NON_EFFECT

    protected constructor(parcel: Parcel) : super(parcel)

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<RandomParams> {
            override fun createFromParcel(source: Parcel) = RandomParams(source)

            override fun newArray(size: Int) = arrayOfNulls<RandomParams>(size)
        }
    }
}