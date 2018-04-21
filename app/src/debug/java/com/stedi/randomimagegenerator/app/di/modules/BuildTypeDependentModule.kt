package com.stedi.randomimagegenerator.app.di.modules

import android.content.Context
import com.stedi.randomimagegenerator.app.di.AppContext
import com.stedi.randomimagegenerator.app.model.repository.CachedPresetRepository
import com.stedi.randomimagegenerator.app.model.repository.DatabasePresetRepository
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BuildTypeDependentModule {

    @Provides
    @Singleton
    fun providePresetRepository(@AppContext context: Context): PresetRepository {
        return CachedPresetRepository(DatabasePresetRepository(context))
    }
}