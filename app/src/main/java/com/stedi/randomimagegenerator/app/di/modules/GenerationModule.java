package com.stedi.randomimagegenerator.app.di.modules;

import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.impl.ApplyGenerationPresenterImpl;
import com.stedi.randomimagegenerator.app.presenter.impl.ChooseEffectPresenterImpl;
import com.stedi.randomimagegenerator.app.presenter.impl.ChooseGeneratorPresenterImpl;
import com.stedi.randomimagegenerator.app.presenter.impl.ChooseSaveOptionsPresenterImpl;
import com.stedi.randomimagegenerator.app.presenter.impl.ChooseSizeAndCountPresenterImpl;
import com.stedi.randomimagegenerator.app.presenter.impl.EditColoredCirclesPresenterImpl;
import com.stedi.randomimagegenerator.app.presenter.impl.GenerationPresenterImpl;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ApplyGenerationPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseEffectPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseGeneratorPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSaveOptionsPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSizeAndCountPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.EditColoredCirclesPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;

@Module
public class GenerationModule {
    @Provides
    GenerationPresenter provideGenerationPresenter(PendingPreset pendingPreset, Logger logger) {
        return new GenerationPresenterImpl(pendingPreset, logger);
    }

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

    @Provides
    ChooseSizeAndCountPresenter provideChooseSizeAndCountPresenter(PendingPreset pendingPreset, Logger logger) {
        return new ChooseSizeAndCountPresenterImpl(pendingPreset, logger);
    }

    @Provides
    ChooseSaveOptionsPresenter provideChooseSaveOptionsPresenter(PendingPreset pendingPreset, Logger logger) {
        return new ChooseSaveOptionsPresenterImpl(pendingPreset, logger);
    }

    @Provides
    ApplyGenerationPresenter provideApplyGenerationPresenter(PendingPreset pendingPreset,
                                                             PresetRepository presetRepository,
                                                             @Named("DefaultScheduler") Scheduler subscribeOn,
                                                             @Named("UiScheduler") Scheduler observeOn,
                                                             CachedBus bus, Logger logger) {
        return new ApplyGenerationPresenterImpl(pendingPreset, presetRepository, subscribeOn, observeOn, bus, logger);
    }
}
