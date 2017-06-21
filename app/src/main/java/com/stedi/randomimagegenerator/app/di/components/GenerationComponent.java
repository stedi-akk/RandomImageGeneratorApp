package com.stedi.randomimagegenerator.app.di.components;

import com.stedi.randomimagegenerator.app.di.modules.GenerationModule;
import com.stedi.randomimagegenerator.app.view.activity.GenerationStepsActivity;
import com.stedi.randomimagegenerator.app.view.dialogs.ColoredNoiseParamsDialog;
import com.stedi.randomimagegenerator.app.view.dialogs.SimpleIntegerParamsDialog;
import com.stedi.randomimagegenerator.app.view.fragments.ApplyGenerationFragment;
import com.stedi.randomimagegenerator.app.view.fragments.ChooseEffectFragment;
import com.stedi.randomimagegenerator.app.view.fragments.ChooseGeneratorFragment;
import com.stedi.randomimagegenerator.app.view.fragments.ChooseSaveOptionsFragment;
import com.stedi.randomimagegenerator.app.view.fragments.ChooseSizeAndCountFragment;

import dagger.Subcomponent;

@Subcomponent(modules = GenerationModule.class)
public interface GenerationComponent {
    void inject(GenerationStepsActivity activity);

    void inject(ChooseGeneratorFragment fragment);

    void inject(SimpleIntegerParamsDialog dialog);

    void inject(ColoredNoiseParamsDialog dialog);

    void inject(ChooseEffectFragment fragment);

    void inject(ChooseSizeAndCountFragment fragment);

    void inject(ChooseSaveOptionsFragment fragment);

    void inject(ApplyGenerationFragment fragment);
}
