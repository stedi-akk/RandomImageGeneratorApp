package com.stedi.randomimagegenerator.app.di.modules;

import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.ChooseEffectPresenter;
import com.stedi.randomimagegenerator.app.presenter.ChooseEffectPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class ChooseEffectModule {
    @Provides
    ChooseEffectPresenter provideChooseEffectPresenter(PendingPreset pendingPreset, Logger logger) {
        return new ChooseEffectPresenterImpl(pendingPreset, logger);
    }
}
