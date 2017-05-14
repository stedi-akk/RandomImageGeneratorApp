package com.stedi.randomimagegenerator.app.di.components;

import com.stedi.randomimagegenerator.app.di.modules.GenerationModule;
import com.stedi.randomimagegenerator.app.view.dialogs.EditColoredCirclesDialog;
import com.stedi.randomimagegenerator.app.view.fragments.ChooseEffectFragment;
import com.stedi.randomimagegenerator.app.view.fragments.ChooseGeneratorFragment;

import dagger.Subcomponent;

@Subcomponent(modules = GenerationModule.class)
public interface GenerationComponent {
    void inject(ChooseGeneratorFragment fragment);

    void inject(EditColoredCirclesDialog dialog);

    void inject(ChooseEffectFragment fragment);
}
