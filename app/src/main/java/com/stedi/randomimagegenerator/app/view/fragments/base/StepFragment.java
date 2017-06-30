package com.stedi.randomimagegenerator.app.view.fragments.base;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.other.Utils;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public abstract class StepFragment extends ButterKnifeFragment implements BlockingStep {
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

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        Utils.hideInput(getActivity());
        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        Utils.hideInput(getActivity());
        callback.goToPrevStep();
    }
}
