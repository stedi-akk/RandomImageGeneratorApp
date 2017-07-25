package com.stedi.randomimagegenerator.app.model.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
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
            TableUtils.createTable(connectionSource, Preset.class);
            TableUtils.createTable(connectionSource, ColoredCirclesParams.class);
            TableUtils.createTable(connectionSource, ColoredNoiseParams.class);
            TableUtils.createTable(connectionSource, ColoredPixelsParams.class);
            TableUtils.createTable(connectionSource, ColoredRectangleParams.class);
            TableUtils.createTable(connectionSource, FlatColorParams.class);
            TableUtils.createTable(connectionSource, MirroredParams.class);
            TableUtils.createTable(connectionSource, TextOverlayParams.class);
        } catch (SQLException e) {
            logger.log(this, e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Preset.class, false);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            logger.log(this, e);
        }
    }

    @Override
    public synchronized void save(@NonNull Preset preset) throws Exception {
        Class paramsClass = preset.getGeneratorParams().getClass();

        Dao<GeneratorParams, Integer> daoParams = getDao(paramsClass);
        Dao.CreateOrUpdateStatus status = daoParams.createOrUpdate(preset.getGeneratorParams());
        if (!status.isCreated() && !status.isUpdated())
            throw new SQLException("failed to save preset");

        preset.setGeneratorParamsId(preset.getGeneratorParams().getId());

        Dao<Preset, Integer> daoPreset = getDao(Preset.class);
        status = daoPreset.createOrUpdate(preset);
        if (!status.isCreated() && !status.isUpdated())
            throw new SQLException("failed to save preset");
    }

    @Override
    public synchronized void remove(int id) throws Exception {
        Dao<Preset, Integer> daoPreset = getDao(Preset.class);
        Preset preset = daoPreset.queryForId(id);
        Class paramsClass = getGeneratorParamsClassFromType(preset.getGeneratorType());
        Dao<GeneratorParams, Integer> daoParams = getDao(paramsClass);
        if (daoParams.deleteById(preset.getGeneratorParamsId()) != 1 || daoPreset.deleteById(id) != 1)
            throw new SQLException("failed to delete preset with id=" + id);
    }

    @Nullable
    @Override
    public synchronized Preset get(int id) throws Exception {
        Dao<Preset, Integer> daoPreset = getDao(Preset.class);
        Preset preset = daoPreset.queryForId(id);
        Class paramsClass = getGeneratorParamsClassFromType(preset.getGeneratorType());
        Dao<GeneratorParams, Integer> daoParams = getDao(paramsClass);
        GeneratorParams generatorParams = daoParams.queryForId(preset.getGeneratorParamsId());
        preset.setGeneratorParams(generatorParams);
        return preset;
    }

    @NonNull
    @Override
    public synchronized List<Preset> getAll() throws Exception {
        Dao<Preset, Integer> dao = getDao(Preset.class);
        List<Preset> presets = dao.queryForAll();
        for (Preset preset : presets) {
            Class paramsClass = getGeneratorParamsClassFromType(preset.getGeneratorType());
            Dao<GeneratorParams, Integer> daoParams = getDao(paramsClass);
            GeneratorParams generatorParams = daoParams.queryForId(preset.getGeneratorParamsId());
            preset.setGeneratorParams(generatorParams);
        }
        return presets;
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
