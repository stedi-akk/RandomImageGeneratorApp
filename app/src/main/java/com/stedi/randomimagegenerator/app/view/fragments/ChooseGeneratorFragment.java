package com.stedi.randomimagegenerator.app.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.di.modules.ChooseGeneratorModule;
import com.stedi.randomimagegenerator.app.presenter.ChooseGeneratorPresenter;
import com.stedi.randomimagegenerator.generators.Generator;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.List;

import javax.inject.Inject;

public class ChooseGeneratorFragment extends BaseFragment implements ChooseGeneratorPresenter.UIImpl, Step {
    @Inject ChooseGeneratorPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBaseActivity().getActivityComponent().plus(new ChooseGeneratorModule()).inject(this);
        presenter.onAttach(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.getGenerators();
    }

    @Override
    public void onShowGeneratorsToChoose(List<Generator> generators) {

    }

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
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
