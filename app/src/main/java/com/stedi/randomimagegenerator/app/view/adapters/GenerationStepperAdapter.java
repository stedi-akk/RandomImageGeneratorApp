package com.stedi.randomimagegenerator.app.view.adapters;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.stedi.randomimagegenerator.app.view.fragments.ApplyGenerationFragment;
import com.stedi.randomimagegenerator.app.view.fragments.ChooseEffectFragment;
import com.stedi.randomimagegenerator.app.view.fragments.ChooseGeneratorFragment;
import com.stedi.randomimagegenerator.app.view.fragments.ChooseSaveOptionsFragment;
import com.stedi.randomimagegenerator.app.view.fragments.ChooseSizeAndCountFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;

public class GenerationStepperAdapter extends AbstractFragmentStepAdapter {
    public GenerationStepperAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(@IntRange(from = 0L) int position) {
        switch (position) {
            case 0:
                return new ChooseGeneratorFragment();
            case 1:
                return new ChooseEffectFragment();
            case 2:
                return new ChooseSizeAndCountFragment();
            case 3:
                return new ChooseSaveOptionsFragment();
            case 4:
                return new ApplyGenerationFragment();
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
