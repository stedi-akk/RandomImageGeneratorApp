package com.stedi.randomimagegenerator.app.di.components;

import com.stedi.randomimagegenerator.app.di.modules.ChooseGeneratorModule;
import com.stedi.randomimagegenerator.app.view.dialogs.EditColoredCirclesDialog;
import com.stedi.randomimagegenerator.app.view.fragments.ChooseGeneratorFragment;

import dagger.Subcomponent;

@Subcomponent(modules = ChooseGeneratorModule.class)
public interface ChooseGeneratorComponent {
    void inject(ChooseGeneratorFragment fragment);

    void inject(EditColoredCirclesDialog dialog);
}
