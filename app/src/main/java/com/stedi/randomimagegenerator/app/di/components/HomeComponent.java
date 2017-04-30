package com.stedi.randomimagegenerator.app.di.components;

import com.stedi.randomimagegenerator.app.di.modules.HomeModule;
import com.stedi.randomimagegenerator.app.view.activity.HomeActivity;

import dagger.Subcomponent;

@Subcomponent(modules = HomeModule.class)
public interface HomeComponent {
    void inject(HomeActivity homeActivity);
}
