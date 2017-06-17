package com.stedi.randomimagegenerator.app.di.components;

import com.stedi.randomimagegenerator.app.di.modules.ActivityModule;
import com.stedi.randomimagegenerator.app.di.modules.GenerationModule;
import com.stedi.randomimagegenerator.app.di.modules.HomeModule;
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity;

import dagger.Subcomponent;

@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(BaseActivity activity);

    HomeComponent plus(HomeModule module);

    GenerationComponent plus(GenerationModule module);
}
