package com.stedi.randomimagegenerator.app.di.modules

import android.content.Context
import com.stedi.randomimagegenerator.app.di.AppContext
import com.stedi.randomimagegenerator.app.model.repository.CachedPresetRepository
import com.stedi.randomimagegenerator.app.model.repository.DatabasePresetRepository
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository
import com.stedi.randomimagegenerator.app.model.repository.SlowPresetRepository
import com.stedi.randomimagegenerator.app.other.logger.LogCatLogger
import com.stedi.randomimagegenerator.app.other.logger.Logger
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BuildTypeDependentModule {
    @Provides
    @Singleton
    fun provideLogger(): Logger {
        return LogCatLogger("RIGAPP-SDBG")
    }

    @Provides
    @Singleton
    fun providePresetRepository(@AppContext context: Context, logger: Logger): PresetRepository {
        return CachedPresetRepository(SlowPresetRepository(DatabasePresetRepository(context, logger)))
    }
}