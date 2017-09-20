package com.stedi.randomimagegenerator.app.view.adapters;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.view.fragments.ApplyGenerationFragment;
import com.stedi.randomimagegenerator.app.view.fragments.ChooseEffectFragment;
import com.stedi.randomimagegenerator.app.view.fragments.ChooseGeneratorFragment;
import com.stedi.randomimagegenerator.app.view.fragments.ChooseSaveOptionsFragment;
import com.stedi.randomimagegenerator.app.view.fragments.ChooseSizeAndCountFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

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
                throw new IllegalStateException("unreachable code");
        }
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0L) int position) {
        StepViewModel.Builder builder = new StepViewModel.Builder(context);
        switch (position) {
            case 0:
                return builder.setEndButtonLabel(R.string.effect).create();
            case 1:
                return builder.setBackButtonLabel(R.string.generator)
                        .setEndButtonLabel(R.string.size_count).create();
            case 2:
                return builder.setBackButtonLabel(R.string.effect)
                        .setEndButtonLabel(R.string.quality).create();
            case 3:
                return builder.setBackButtonLabel(R.string.size_count)
                        .setEndButtonLabel(R.string.summary).create();
            case 4:
                return builder.setBackButtonLabel(R.string.configure)
                        .setEndButtonVisible(false).create();
            default:
                throw new IllegalStateException("unreachable code");
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
