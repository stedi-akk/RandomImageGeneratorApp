package com.stedi.randomimagegenerator.app.di.modules;

import com.stedi.randomimagegenerator.app.other.logger.LogCatLogger;
import com.stedi.randomimagegenerator.app.other.logger.Logger;

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
}
