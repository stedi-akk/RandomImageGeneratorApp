package com.stedi.randomimagegenerator.app.di.modules;

import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.ChooseEffectPresenter;
import com.stedi.randomimagegenerator.app.presenter.ChooseEffectPresenterImpl;
import com.stedi.randomimagegenerator.app.presenter.ChooseGeneratorPresenter;
import com.stedi.randomimagegenerator.app.presenter.ChooseGeneratorPresenterImpl;
import com.stedi.randomimagegenerator.app.presenter.EditColoredCirclesPresenter;
import com.stedi.randomimagegenerator.app.presenter.EditColoredCirclesPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class GenerationModule {
    @Provides
    ChooseGeneratorPresenter provideChooseGeneratorPresenter(PendingPreset pendingPreset, Logger logger) {
        return new ChooseGeneratorPresenterImpl(pendingPreset, logger);
    }

    @Provides
    EditColoredCirclesPresenter provideEditColoredCirclesPresenter(PendingPreset pendingPreset, Logger logger) {
        return new EditColoredCirclesPresenterImpl(pendingPreset, logger);
    }

    @Provides
    ChooseEffectPresenter provideChooseEffectPresenter(PendingPreset pendingPreset, Logger logger) {
        return new ChooseEffectPresenterImpl(pendingPreset, logger);
    }
}
