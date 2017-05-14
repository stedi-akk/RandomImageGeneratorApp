package com.stedi.randomimagegenerator.app.di.components;

import com.stedi.randomimagegenerator.app.di.modules.ChooseEffectModule;
import com.stedi.randomimagegenerator.app.view.fragments.ChooseEffectFragment;

import dagger.Subcomponent;

@Subcomponent(modules = ChooseEffectModule.class)
public interface ChooseEffectComponent {
    void inject(ChooseEffectFragment fragment);
}
