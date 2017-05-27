package com.stedi.randomimagegenerator.app.di.modules;

import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.other.logger.NullLogger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BuildTypeDependentModule {
    @Provides
    @Singleton
    Logger provideLogger() {
        return new NullLogger();
    }

    @Provides
    @Singleton
    PresetRepository providePresetRepository() {
        return new CachedPresetRepository(new DatabasePresetRepository());
    }
}
