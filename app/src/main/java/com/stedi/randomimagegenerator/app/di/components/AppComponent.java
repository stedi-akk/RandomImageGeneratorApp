package com.stedi.randomimagegenerator.app.di.components;

import com.stedi.randomimagegenerator.app.di.modules.ActivityModule;
import com.stedi.randomimagegenerator.app.di.modules.AppModule;
import com.stedi.randomimagegenerator.app.di.modules.BuildTypeDependentModule;
import com.stedi.randomimagegenerator.app.view.activity.base.LifeCycleActivity;
import com.stedi.randomimagegenerator.app.view.dialogs.ConfirmDialog;
import com.stedi.randomimagegenerator.app.view.fragments.base.LifeCycleFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, BuildTypeDependentModule.class})
@Singleton
public interface AppComponent {
    ActivityComponent plus(ActivityModule module);

    void inject(LifeCycleFragment fragment);

    void inject(LifeCycleActivity fragment);

    void inject(ConfirmDialog dialog);
}
