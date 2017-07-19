package com.stedi.randomimagegenerator.app.di.modules;

import android.content.Context;

import com.stedi.randomimagegenerator.app.model.repository.CachedPresetRepository;
import com.stedi.randomimagegenerator.app.model.repository.DatabasePresetRepository;
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository;
import com.stedi.randomimagegenerator.app.other.logger.LogCatLogger;
import com.stedi.randomimagegenerator.app.other.logger.Logger;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BuildTypeDependentModule {
    @Provides
    @Singleton
    Logger provideLogger() {
        return new LogCatLogger("RIGAPP-DBG");
    }

    @Provides
    @Singleton
    PresetRepository providePresetRepository(@Named("AppContext") Context context, Logger logger) {
        return new CachedPresetRepository(new DatabasePresetRepository(context, logger));
    }
}
