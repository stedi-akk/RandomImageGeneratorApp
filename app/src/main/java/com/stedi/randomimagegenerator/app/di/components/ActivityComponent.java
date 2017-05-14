package com.stedi.randomimagegenerator.app.di.components;

import com.stedi.randomimagegenerator.app.di.modules.ActivityModule;
import com.stedi.randomimagegenerator.app.di.modules.ChooseEffectModule;
import com.stedi.randomimagegenerator.app.di.modules.ChooseGeneratorModule;
import com.stedi.randomimagegenerator.app.di.modules.HomeModule;

import dagger.Subcomponent;

@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    HomeComponent plus(HomeModule module);

    ChooseGeneratorComponent plus(ChooseGeneratorModule module);

    ChooseEffectComponent plus(ChooseEffectModule module);
}
