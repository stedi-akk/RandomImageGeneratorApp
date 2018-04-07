package com.stedi.randomimagegenerator.app.model.data.generatorparams.base

import android.os.Parcel
import com.j256.ormlite.field.DatabaseField

abstract class SimpleIntegerParams : GeneratorParams {

    @DatabaseField(columnName = "integer_value")
    private var value: Int? = if (canBeRandom()) null else 1

    // OrmLite required
    constructor()

    fun setRandomValue() {
        if (canBeRandom()) {
            this.value = null
        }
    }

    fun setValue(value: Int) {
        if (value < 1) {
            throw IllegalArgumentException("value must be > 0")
        }
        this.value = value
    }

    fun getValue(): Int? = value

    abstract fun canBeRandom(): Boolean

    override fun isEditable() = true

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false
        val that = other as SimpleIntegerParams
        return if (value != null) value == that.value else that.value == null
    }

    override fun hashCode() = value?.hashCode() ?: 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(this.value)
    }

    protected constructor(parcel: Parcel) {
        this.value = parcel.readValue(Int::class.java.classLoader) as Int
    }
}
