package com.stedi.randomimagegenerator.app.model.data.generatorparams.base

import android.os.Parcel
import android.support.annotation.CallSuper
import com.j256.ormlite.field.DatabaseField

abstract class SimpleIntegerParams : GeneratorParams {

    @Suppress("LeakingThis")
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

    @CallSuper
    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false
        val that = other as SimpleIntegerParams
        return if (value != null) value == that.value else that.value == null
    }

    @CallSuper
    override fun hashCode() = value?.hashCode() ?: 0

    @CallSuper
    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeValue(this.value)
    }

    protected constructor(parcel: Parcel) : super(parcel) {
        this.value = parcel.readValue(Int::class.java.classLoader) as Int?
    }
}
