package com.stedi.randomimagegenerator.app.di.modules

import com.stedi.randomimagegenerator.app.di.DefaultScheduler
import com.stedi.randomimagegenerator.app.di.RigScheduler
import com.stedi.randomimagegenerator.app.di.UiScheduler
import com.stedi.randomimagegenerator.app.model.data.PendingPreset
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository
import com.stedi.randomimagegenerator.app.other.CachedBus
import com.stedi.randomimagegenerator.app.other.logger.Logger
import com.stedi.randomimagegenerator.app.presenter.impl.HomePresenterImpl
import com.stedi.randomimagegenerator.app.presenter.interfaces.HomePresenter
import dagger.Module
import dagger.Provides
import rx.Scheduler

@Module
class HomeModule {

    @Provides
    fun provideHomePresenter(repository: PresetRepository,
                             pendingPreset: PendingPreset,
                             @DefaultScheduler subscribeOn: Scheduler,
                             @RigScheduler superSubscribeOn: Scheduler,
                             @UiScheduler observeOn: Scheduler,
                             bus: CachedBus,
                             logger: Logger): HomePresenter {
        return HomePresenterImpl(repository, pendingPreset, subscribeOn, superSubscribeOn, observeOn, bus, logger)
    }
}