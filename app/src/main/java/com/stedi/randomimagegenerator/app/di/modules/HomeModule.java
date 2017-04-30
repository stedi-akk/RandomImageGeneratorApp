package com.stedi.randomimagegenerator.app.di.modules;

import com.squareup.otto.Bus;
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.HomePresenter;
import com.stedi.randomimagegenerator.app.presenter.HomePresenterImpl;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;

@Module
public class HomeModule {
    @Provides
    HomePresenter provideHomePresenter(PresetRepository repository,
                                       @Named("DefaultScheduler") Scheduler subscribeOn,
                                       @Named("UiScheduler") Scheduler observeOn,
                                       Bus bus,
                                       Logger logger) {
        return new HomePresenterImpl(repository, subscribeOn, observeOn, bus, logger);
    }
}
