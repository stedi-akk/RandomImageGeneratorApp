package com.stedi.randomimagegenerator.app.view.fragments;

import android.support.annotation.NonNull;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class ChooseGeneratorFragment extends BaseFragment implements Step {
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
