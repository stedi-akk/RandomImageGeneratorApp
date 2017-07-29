package com.stedi.randomimagegenerator.app.model.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.ColoredCirclesParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.ColoredNoiseParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.ColoredPixelsParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.ColoredRectangleParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.FlatColorParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.MirroredParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.TextOverlayParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.logger.Logger;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Named;

public class DatabasePresetRepository extends OrmLiteSqliteOpenHelper implements PresetRepository {
    private static final String DATABASE_NAME = "presets_database";
    private static final int DATABASE_VERSION = 1;

    private final Logger logger;

    public DatabasePresetRepository(@Named("AppContext") Context context, @NonNull Logger logger) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.logger = logger;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Preset.class);
            for (GeneratorType type : GeneratorType.values())
                TableUtils.createTableIfNotExists(connectionSource, getGeneratorParamsClassFromType(type));
        } catch (Exception e) {
            logger.log(this, e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Preset.class, false);
            for (GeneratorType type : GeneratorType.values())
                TableUtils.dropTable(connectionSource, getGeneratorParamsClassFromType(type), false);
            onCreate(database, connectionSource);
        } catch (Exception e) {
            logger.log(this, e);
        }
    }

    @Override
    public synchronized void save(@NonNull Preset preset) throws Exception {
        TransactionManager.callInTransaction(getConnectionSource(), () -> {
            saveGeneratorParamsRecursively(preset.getGeneratorParams());
            preset.setGeneratorParamsId(preset.getGeneratorParams().getId());
            Dao<Preset, Integer> daoPreset = getDao(Preset.class);
            Dao.CreateOrUpdateStatus status = daoPreset.createOrUpdate(preset);
            if (!status.isCreated() && !status.isUpdated())
                throw new SQLException("failed to save or update preset");
            return true;
        });
    }

    @Override
    public synchronized void remove(int id) throws Exception {
        TransactionManager.callInTransaction(getConnectionSource(), () -> {
            Dao<Preset, Integer> daoPreset = getDao(Preset.class);
            Preset preset = daoPreset.queryForId(id);
            deleteGeneratorParamsRecursively(queryGeneratorParamsById(preset.getGeneratorType(), preset.getGeneratorParamsId()));
            if (daoPreset.delete(preset) != 1)
                throw new SQLException("failed to delete preset with id=" + id);
            return true;
        });
    }

    @Nullable
    @Override
    public synchronized Preset get(int id) throws Exception {
        Dao<Preset, Integer> daoPreset = getDao(Preset.class);
        Preset preset = daoPreset.queryForId(id);
        GeneratorParams generatorParams = queryGeneratorParamsById(preset.getGeneratorType(), preset.getGeneratorParamsId());
        fillGeneratorParamsRecursively(generatorParams);
        preset.setGeneratorParams(generatorParams);
        return preset;
    }

    @NonNull
    @Override
    public synchronized List<Preset> getAll() throws Exception {
        Dao<Preset, Integer> dao = getDao(Preset.class);
        List<Preset> presets = dao.queryForAll();
        for (Preset preset : presets) {
            GeneratorParams generatorParams = queryGeneratorParamsById(preset.getGeneratorType(), preset.getGeneratorParamsId());
            fillGeneratorParamsRecursively(generatorParams);
            preset.setGeneratorParams(generatorParams);
        }
        return presets;
    }

    private void saveGeneratorParamsRecursively(GeneratorParams generatorParams) throws Exception {
        if (generatorParams instanceof EffectGeneratorParams) {
            EffectGeneratorParams effectParams = (EffectGeneratorParams) generatorParams;
            GeneratorParams targetParams = effectParams.getTarget();
            saveGeneratorParamsRecursively(targetParams);
            effectParams.setTargetGeneratorParamsId(targetParams.getId());
        }
        Class paramsClass = generatorParams.getClass();
        Dao<GeneratorParams, Integer> daoParams = getDao(paramsClass);
        Dao.CreateOrUpdateStatus status = daoParams.createOrUpdate(generatorParams);
        if (!status.isCreated() && !status.isUpdated())
            throw new SQLException("failed to save generator params " + generatorParams);
    }

    private void deleteGeneratorParamsRecursively(GeneratorParams generatorParams) throws Exception {
        if (generatorParams instanceof EffectGeneratorParams) {
            EffectGeneratorParams effectParams = (EffectGeneratorParams) generatorParams;
            deleteGeneratorParamsRecursively(queryGeneratorParamsById(effectParams.getTargetGeneratorType(), effectParams.getTargetGeneratorParamsId()));
        }
        Class paramsClass = generatorParams.getClass();
        Dao<GeneratorParams, Integer> daoParams = getDao(paramsClass);
        if (daoParams.delete(generatorParams) != 1)
            throw new SQLException("failed to delete generator params " + generatorParams);
    }

    private void fillGeneratorParamsRecursively(GeneratorParams generatorParams) throws Exception {
        if (generatorParams instanceof EffectGeneratorParams) {
            EffectGeneratorParams effectParams = (EffectGeneratorParams) generatorParams;
            GeneratorParams targetParams = queryGeneratorParamsById(effectParams.getTargetGeneratorType(), effectParams.getTargetGeneratorParamsId());
            fillGeneratorParamsRecursively(targetParams);
            effectParams.setTarget(targetParams);
        }
    }

    private GeneratorParams queryGeneratorParamsById(GeneratorType type, int id) throws Exception {
        Class paramsClass = getGeneratorParamsClassFromType(type);
        Dao<GeneratorParams, Integer> daoParams = getDao(paramsClass);
        return daoParams.queryForId(id);
    }

    private Class<? extends GeneratorParams> getGeneratorParamsClassFromType(GeneratorType type) throws Exception {
        switch (type) {
            case FLAT_COLOR:
                return FlatColorParams.class;
            case COLORED_PIXELS:
                return ColoredPixelsParams.class;
            case COLORED_CIRCLES:
                return ColoredCirclesParams.class;
            case COLORED_RECTANGLE:
                return ColoredRectangleParams.class;
            case COLORED_NOISE:
                return ColoredNoiseParams.class;
            case MIRRORED:
                return MirroredParams.class;
            case TEXT_OVERLAY:
                return TextOverlayParams.class;
            default:
                throw new IllegalStateException("unreachable code");
        }
    }
}
