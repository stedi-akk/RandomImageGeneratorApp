package com.stedi.randomimagegenerator.app.di.modules;

import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.ChooseGeneratorPresenter;
import com.stedi.randomimagegenerator.app.presenter.ChooseGeneratorPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class ChooseGeneratorModule {
    @Provides
    ChooseGeneratorPresenter provideChooseGeneratorPresenter(PendingPreset pendingPreset, Logger logger) {
        return new ChooseGeneratorPresenterImpl(pendingPreset, logger);
    }
}
