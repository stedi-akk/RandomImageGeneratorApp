package com.stedi.randomimagegenerator.app.di.modules;

import com.stedi.randomimagegenerator.app.di.DefaultScheduler;
import com.stedi.randomimagegenerator.app.di.RigScheduler;
import com.stedi.randomimagegenerator.app.di.RootSavePath;
import com.stedi.randomimagegenerator.app.di.UiScheduler;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.impl.ApplyGenerationPresenterImpl;
import com.stedi.randomimagegenerator.app.presenter.impl.ChooseEffectPresenterImpl;
import com.stedi.randomimagegenerator.app.presenter.impl.ChooseGeneratorPresenterImpl;
import com.stedi.randomimagegenerator.app.presenter.impl.ChooseSaveOptionsPresenterImpl;
import com.stedi.randomimagegenerator.app.presenter.impl.ChooseSizeAndCountPresenterImpl;
import com.stedi.randomimagegenerator.app.presenter.impl.ColoredNoiseParamsPresenterImpl;
import com.stedi.randomimagegenerator.app.presenter.impl.GenerationStepsPresenterImpl;
import com.stedi.randomimagegenerator.app.presenter.impl.SimpleIntegerParamsPresenterImpl;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ApplyGenerationPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseEffectPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseGeneratorPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSaveOptionsPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSizeAndCountPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ColoredNoiseParamsPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationStepsPresenter;
import com.stedi.randomimagegenerator.app.presenter.interfaces.SimpleIntegerParamsPresenter;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;

@Module
public class GenerationModule {
    @Provides
    GenerationStepsPresenter provideGenerationStepsPresenter(PendingPreset pendingPreset, Logger logger) {
        return new GenerationStepsPresenterImpl(pendingPreset, logger);
    }

    @Provides
    ChooseGeneratorPresenter provideChooseGeneratorPresenter(PendingPreset pendingPreset, Logger logger) {
        return new ChooseGeneratorPresenterImpl(pendingPreset, logger);
    }

    @Provides
    SimpleIntegerParamsPresenter provideEditColoredCirclesPresenter(PendingPreset pendingPreset, Logger logger) {
        return new SimpleIntegerParamsPresenterImpl(pendingPreset, logger);
    }

    @Provides
    ColoredNoiseParamsPresenter provideColoredNoiseParamsPresenter(PendingPreset pendingPreset) {
        return new ColoredNoiseParamsPresenterImpl(pendingPreset);
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
                                                             @RootSavePath String rootSavePath,
                                                             @RigScheduler Scheduler superSubscribeOn,
                                                             @DefaultScheduler Scheduler subscribeOn,
                                                             @UiScheduler Scheduler observeOn,
                                                             CachedBus bus, Logger logger) {
        return new ApplyGenerationPresenterImpl(pendingPreset, presetRepository, rootSavePath, superSubscribeOn, subscribeOn, observeOn, bus, logger);
    }
}
