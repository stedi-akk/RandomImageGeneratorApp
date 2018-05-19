package com.stedi.randomimagegenerator.app.model.data.generatorparams

import android.os.Parcel
import android.os.Parcelable
import com.j256.ormlite.table.DatabaseTable
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams
import com.stedi.randomimagegenerator.generators.Generator
import com.stedi.randomimagegenerator.generators.effects.ThresholdEffect

@DatabaseTable(tableName = "threshold_params")
class ThresholdParams : EffectGeneratorParams {

    // OrmLite required
    constructor()

    constructor(target: GeneratorParams) : super(target)

    override fun createEffectGenerator(target: Generator) = ThresholdEffect(target)

    override fun getType() = GeneratorType.THRESHOLD

    protected constructor(parcel: Parcel) : super(parcel)

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<ThresholdParams> {
            override fun createFromParcel(source: Parcel) = ThresholdParams(source)

            override fun newArray(size: Int) = arrayOfNulls<ThresholdParams>(size)
        }
    }
}