package com.stedi.randomimagegenerator.app.model.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.misc.TransactionManager
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.stedi.randomimagegenerator.app.di.AppContext
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.model.data.generatorparams.*
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams
import timber.log.Timber
import java.sql.SQLException

class DatabasePresetRepository(@AppContext context: Context) : OrmLiteSqliteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION), PresetRepository {

    companion object {
        const val DATABASE_NAME = "presets_database"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(database: SQLiteDatabase, connectionSource: ConnectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Preset::class.java)
            for (type in GeneratorType.values()) {
                TableUtils.createTableIfNotExists(connectionSource, getGeneratorParamsClassFromType(type))
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onUpgrade(database: SQLiteDatabase, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int) {
    }

    @Throws(Exception::class)
    @Synchronized
    override fun save(preset: Preset) {
        TransactionManager.callInTransaction(getConnectionSource()) {
            deleteGeneratorParams(preset)
            saveGeneratorParams(preset.getGeneratorParams())
            preset.generatorParamsId = preset.getGeneratorParams().id
            val status = getDao<Dao<Preset, Int>, Preset>(Preset::class.java).createOrUpdate(preset)
            if (!status.isCreated && !status.isUpdated) {
                throw SQLException("failed to save or update preset")
            }
            true
        }
    }

    @Throws(Exception::class)
    @Synchronized
    override fun remove(id: Int) {
        TransactionManager.callInTransaction(getConnectionSource()) {
            val daoPreset = getDao<Dao<Preset, Int>, Preset>(Preset::class.java)
            val preset = daoPreset.queryForId(id)
            if (preset != null) {
                deleteGeneratorParams(queryGeneratorParamsById(preset.getGeneratorType(), preset.generatorParamsId))
                if (daoPreset.delete(preset) != 1) {
                    throw SQLException("failed to delete preset with id=$id")
                }
            }
            true
        }
    }

    @Throws(Exception::class)
    @Synchronized
    override fun get(id: Int): Preset? {
        val preset = getDao<Dao<Preset, Int>, Preset>(Preset::class.java).queryForId(id)
        if (preset != null) {
            queryGeneratorParamsById(preset.getGeneratorType(), preset.generatorParamsId).apply {
                fillEffectParams(this)
                preset.setGeneratorParams(this)
            }
        }
        return preset
    }

    @Throws(Exception::class)
    @Synchronized
    override fun getAll(): List<Preset> {
        val presets = getDao<Dao<Preset, Int>, Preset>(Preset::class.java).queryForAll()
        for (preset in presets) {
            queryGeneratorParamsById(preset.getGeneratorType(), preset.generatorParamsId).apply {
                fillEffectParams(this)
                preset.setGeneratorParams(this)
            }
        }
        return presets
    }

    @Throws(Exception::class)
    private fun saveGeneratorParams(generatorParams: GeneratorParams) {
        if (generatorParams is EffectGeneratorParams) {
            val targetParams = generatorParams.target
            saveGeneratorParams(targetParams)
            generatorParams.targetGeneratorParamsId = targetParams.id
        }
        val status = getDao(generatorParams.javaClass).createOrUpdate(generatorParams)
        if (!status.isCreated && !status.isUpdated) {
            throw SQLException("failed to save generator params $generatorParams")
        }
    }

    @Throws(Exception::class)
    private fun deleteGeneratorParams(generatorParams: GeneratorParams) {
        if (generatorParams is EffectGeneratorParams) {
            queryGeneratorParamsById(generatorParams.targetGeneratorType, generatorParams.targetGeneratorParamsId).apply {
                deleteGeneratorParams(this)
            }
        }
        if (getDao(generatorParams.javaClass).delete(generatorParams) != 1) {
            throw SQLException("failed to delete generator params $generatorParams")
        }
    }

    @Throws(Exception::class)
    private fun fillEffectParams(generatorParams: GeneratorParams) {
        if (generatorParams is EffectGeneratorParams) {
            queryGeneratorParamsById(generatorParams.targetGeneratorType, generatorParams.targetGeneratorParamsId).apply {
                fillEffectParams(this)
                generatorParams.setTargetParams(this)
            }
        }
    }

    @Throws(Exception::class)
    private fun deleteGeneratorParams(preset: Preset) {
        getDao<Dao<Preset, Int>, Preset>(Preset::class.java).queryForId(preset.id)?.apply {
            queryGeneratorParamsById(this.getGeneratorType(), this.generatorParamsId).apply {
                deleteGeneratorParams(this)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(Exception::class)
    private fun queryGeneratorParamsById(type: GeneratorType, id: Int): GeneratorParams {
        val paramsClass = getGeneratorParamsClassFromType(type)
        val daoParams = getDao(paramsClass) as Dao<GeneratorParams, Int>
        return daoParams.queryForId(id)
    }

    private fun getGeneratorParamsClassFromType(type: GeneratorType): Class<out GeneratorParams> {
        return when (type) {
            GeneratorType.FLAT_COLOR -> FlatColorParams::class.java
            GeneratorType.COLORED_PIXELS -> ColoredPixelsParams::class.java
            GeneratorType.COLORED_CIRCLES -> ColoredCirclesParams::class.java
            GeneratorType.COLORED_RECTANGLE -> ColoredRectangleParams::class.java
            GeneratorType.COLORED_NOISE -> ColoredNoiseParams::class.java
            GeneratorType.MIRRORED -> MirroredParams::class.java
            GeneratorType.TEXT_OVERLAY -> TextOverlayParams::class.java
        }
    }
}