package com.stedi.randomimagegenerator.app.model.data.generatorparams.base

import android.os.Parcel
import com.j256.ormlite.field.DatabaseField
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.generators.Generator

abstract class EffectGeneratorParams : GeneratorParams {

    lateinit var target: GeneratorParams
        private set

    @DatabaseField(columnName = "target_generator_type", canBeNull = false)
    lateinit var targetGeneratorType: GeneratorType
        private set

    @DatabaseField(columnName = "target_generator_params_id")
    var targetGeneratorParamsId: Int = 0

    // OrmLite required
    constructor()

    constructor(target: GeneratorParams) {
        setTargetParams(target)
    }

    fun setTargetParams(target: GeneratorParams) {
        this.target = target
        this.targetGeneratorType = target.getType()
    }

    override fun createGenerator() = createEffectGenerator(target.getGenerator())

    protected abstract fun createEffectGenerator(target: Generator): Generator

    override fun toString() = "EffectGeneratorParams{" +
            "getType()=${getType()}" +
            ", target=$target" +
            '}'

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false
        val that = other as EffectGeneratorParams
        return target == that.target
    }

    override fun hashCode() = target.hashCode()

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeParcelable(this.target, flags)
        dest.writeInt(this.targetGeneratorParamsId)
    }

    protected constructor(parcel: Parcel) : super(parcel) {
        setTargetParams(parcel.readParcelable(GeneratorParams::class.java.classLoader))
        this.targetGeneratorParamsId = parcel.readInt()
    }
}