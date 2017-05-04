package com.stedi.randomimagegenerator.app.di.modules;

import com.stedi.randomimagegenerator.app.presenter.ChooseGeneratorPresenter;
import com.stedi.randomimagegenerator.app.presenter.ChooseGeneratorPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class ChooseGeneratorModule {
    @Provides
    ChooseGeneratorPresenter provideChooseGeneratorPresenter() {
        return new ChooseGeneratorPresenterImpl();
    }
}
