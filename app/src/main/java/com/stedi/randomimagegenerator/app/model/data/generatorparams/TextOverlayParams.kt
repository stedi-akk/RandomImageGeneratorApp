package com.stedi.randomimagegenerator.app.model.data.generatorparams

import android.os.Parcel
import android.os.Parcelable
import com.j256.ormlite.table.DatabaseTable
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams
import com.stedi.randomimagegenerator.generators.Generator
import com.stedi.randomimagegenerator.generators.effects.TextOverlayEffect

@DatabaseTable(tableName = "text_overlay_params")
class TextOverlayParams : EffectGeneratorParams {

    // OrmLite required
    constructor()

    constructor(target: GeneratorParams) : super(target)

    override fun createEffectGenerator(target: Generator): Generator {
        return TextOverlayEffect.Builder()
                .setGenerator(target)
                .build()
    }

    override fun getType() = GeneratorType.TEXT_OVERLAY

    protected constructor(parcel: Parcel) : super(parcel)

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<TextOverlayParams> {
            override fun createFromParcel(source: Parcel) = TextOverlayParams(source)

            override fun newArray(size: Int) = arrayOfNulls<TextOverlayParams>(size)
        }
    }
}