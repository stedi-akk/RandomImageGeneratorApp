package com.stedi.randomimagegenerator.app.model.data.generatorparams

import android.os.Parcel
import android.os.Parcelable
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.stedi.randomimagegenerator.DefaultFileNamePolicy
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams
import com.stedi.randomimagegenerator.generators.Generator
import com.stedi.randomimagegenerator.generators.TextOverlayGenerator

@DatabaseTable(tableName = "text_overlay_params")
class TextOverlayParams : EffectGeneratorParams {

    @DatabaseField(generatedId = true)
    private var id: Int = 0

    // OrmLite required
    constructor()

    constructor(target: GeneratorParams) : super(target)

    override fun createEffectGenerator(target: Generator): Generator {
        val fileNamePolicy = DefaultFileNamePolicy()
        return TextOverlayGenerator.Builder()
                .setGenerator(target)
                .setTextPolicy { fileNamePolicy.getName(it) }
                .build()
    }

    override fun setId(id: Int) {
        this.id = id
    }

    override fun getId() = id

    override fun isEditable() = false

    override fun getType() = GeneratorType.TEXT_OVERLAY

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeInt(this.id)
    }

    protected constructor(parcel: Parcel) : super(parcel) {
        this.id = parcel.readInt()
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<TextOverlayParams> {
            override fun createFromParcel(source: Parcel) = TextOverlayParams(source)

            override fun newArray(size: Int) = arrayOfNulls<TextOverlayParams>(size)
        }
    }
}