package com.stedi.randomimagegenerator.app.model.data

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.stedi.randomimagegenerator.Quality
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams
import java.util.*

@DatabaseTable(tableName = "preset")
class Preset : Parcelable {

    @DatabaseField(generatedId = true)
    var id: Int = 0

    @DatabaseField(columnName = "timestamp")
    var timestamp: Long = 0

    @DatabaseField(columnName = "name", canBeNull = false)
    var name: String = ""

    private var generatorParams: GeneratorParams? = null

    @DatabaseField(columnName = "generator_type", canBeNull = false)
    private var generatorType: GeneratorType? = null

    @DatabaseField(columnName = "generator_params_id")
    var generatorParamsId: Int = 0

    private var quality: Quality? = null

    @DatabaseField(columnName = "quality_format", canBeNull = false)
    private var qualityFormat: Bitmap.CompressFormat? = null

    @DatabaseField(columnName = "quality_value")
    private var qualityValue: Int = 0

    @DatabaseField(columnName = "path_to_save", canBeNull = false)
    var pathToSave: String = ""

    @DatabaseField(columnName = "count")
    private var count = 1

    @DatabaseField(columnName = "width")
    private var width = 1

    @DatabaseField(columnName = "height")
    private var height = 1

    @DatabaseField(columnName = "width_range", dataType = DataType.SERIALIZABLE)
    private var widthRange: IntArray? = null

    @DatabaseField(columnName = "height_range", dataType = DataType.SERIALIZABLE)
    private var heightRange: IntArray? = null

    // OrmLite required
    constructor()

    constructor(name: String,
                generatorParams: GeneratorParams,
                quality: Quality,
                pathToSave: String) {
        this.name = name
        setGeneratorParams(generatorParams)
        setQuality(quality)
        this.pathToSave = pathToSave
    }

    fun createCopy(): Preset {
        var parcel: Parcel? = null
        try {
            parcel = Parcel.obtain()
            writeToParcel(parcel, 0)
            parcel.setDataPosition(0)
            return CREATOR.createFromParcel(parcel)
        } finally {
            if (parcel != null) {
                parcel.recycle()
            }
        }
    }

    fun clearIds() {
        id = 0
        generatorParamsId = 0
        generatorParams!!.setId(0)
        if (generatorParams is EffectGeneratorParams) {
            val effectGeneratorParams = generatorParams as EffectGeneratorParams
            effectGeneratorParams.targetGeneratorParamsId = 0
            effectGeneratorParams.target.setId(0)
        }
    }

    fun setGeneratorParams(generatorParams: GeneratorParams) {
        this.generatorParams = generatorParams
        this.generatorType = generatorParams.getType()
    }

    fun getGeneratorParams(): GeneratorParams = generatorParams!!

    fun getGeneratorType(): GeneratorType = generatorType!!

    fun setQuality(quality: Quality) {
        this.quality = quality
        this.qualityFormat = quality.format
        this.qualityValue = quality.qualityValue
    }

    fun getQuality(): Quality {
        if (quality == null) {
            quality = Quality(qualityFormat, qualityValue)
        }
        return quality!!
    }

    fun setCount(count: Int) {
        if (widthRange != null || heightRange != null) {
            throw IllegalStateException("count can not be set with size range")
        }
        if (count < 1) {
            throw IllegalArgumentException("count must be > 0")
        }
        this.count = count
    }

    fun getCount() = count

    fun setWidth(width: Int) {
        if (width < 1) {
            throw IllegalArgumentException("width must be > 0")
        }
        this.width = width
        widthRange = null
    }

    fun getWidth() = width

    fun setHeight(height: Int) {
        if (height < 1) {
            throw IllegalArgumentException("height must be > 0")
        }
        this.height = height
        heightRange = null
    }

    fun getHeight() = height

    fun setWidthRange(from: Int, to: Int, step: Int) {
        if (step <= 0 || from <= 0 || to <= 0) {
            throw IllegalArgumentException("all width range args must be bigger than 0")
        }
        widthRange = intArrayOf(from, to, step)
        width = 0
        count = 0
    }

    fun getWidthRange(): IntArray? = widthRange

    fun setHeightRange(from: Int, to: Int, step: Int) {
        if (step <= 0 || from <= 0 || to <= 0) {
            throw IllegalArgumentException("all height range args must be bigger than 0")
        }
        heightRange = intArrayOf(from, to, step)
        height = 0
        count = 0
    }

    fun getHeightRange(): IntArray? = heightRange

    override fun toString() = "Preset{" +
            "id=$id" +
            ", timestamp=$timestamp" +
            ", name='$name'" +
            ", generatorParams=$generatorParams" +
            ", quality=${getQuality()}" +
            ", pathToSave='$pathToSave'" +
            ", count=$count" +
            ", width=$width" +
            ", height=$height" +
            ", widthRange=${Arrays.toString(widthRange)}" +
            ", heightRange=${Arrays.toString(heightRange)}" +
            '}'

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val preset = other as Preset
        if (timestamp != preset.timestamp) return false
        if (count != preset.count) return false
        if (width != preset.width) return false
        if (height != preset.height) return false
        if (name != preset.name) return false
        if (generatorParams != preset.generatorParams) return false
        if (getQuality().format != preset.getQuality().format) return false
        if (getQuality().qualityValue != preset.getQuality().qualityValue) return false
        if (pathToSave != preset.pathToSave) return false
        return if (!Arrays.equals(widthRange, preset.widthRange)) false else Arrays.equals(heightRange, preset.heightRange)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (timestamp xor timestamp.ushr(32)).toInt()
        result = 31 * result + generatorParams!!.hashCode()
        result = 31 * result + getQuality().format.ordinal
        result = 31 * result + getQuality().qualityValue
        result = 31 * result + pathToSave.hashCode()
        result = 31 * result + count
        result = 31 * result + width
        result = 31 * result + height
        result = 31 * result + Arrays.hashCode(widthRange)
        result = 31 * result + Arrays.hashCode(heightRange)
        return result
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(this.id)
        dest.writeInt(this.generatorParamsId)
        dest.writeLong(this.timestamp)
        dest.writeString(this.name)
        dest.writeParcelable(this.generatorParams, flags)
        dest.writeInt(this.getQuality().format.ordinal)
        dest.writeInt(this.getQuality().qualityValue)
        dest.writeString(this.pathToSave)
        dest.writeInt(this.count)
        dest.writeInt(this.width)
        dest.writeInt(this.height)
        dest.writeIntArray(this.widthRange)
        dest.writeIntArray(this.heightRange)
    }

    protected constructor(parcel: Parcel) {
        this.id = parcel.readInt()
        this.generatorParamsId = parcel.readInt()
        this.timestamp = parcel.readLong()
        this.name = parcel.readString()
        setGeneratorParams(parcel.readParcelable(GeneratorParams::class.java.classLoader))
        setQuality(Quality(Bitmap.CompressFormat.values()[parcel.readInt()], parcel.readInt()))
        this.pathToSave = parcel.readString()
        this.count = parcel.readInt()
        this.width = parcel.readInt()
        this.height = parcel.readInt()
        this.widthRange = parcel.createIntArray()
        this.heightRange = parcel.createIntArray()
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<Preset> {
            override fun createFromParcel(source: Parcel) = Preset(source)

            override fun newArray(size: Int) = arrayOfNulls<Preset>(size)
        }
    }
}