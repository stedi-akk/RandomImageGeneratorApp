package com.stedi.randomimagegenerator.app.view.fragments.base;

import android.support.annotation.NonNull;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public abstract class StepFragment extends ButterKnifeFragment implements Step {
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
    }

    @Override
    public void onError(@NonNull VerificationError error) {
    }
}
