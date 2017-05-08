package com.stedi.randomimagegenerator.app.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.di.modules.ChooseGeneratorModule;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.presenter.ChooseGeneratorPresenter;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

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
        presenter.getGeneratorTypes();
    }

    @Override
    public void showTypesToChoose(@NonNull GeneratorType[] types) {

    }

    @Override
    public void onGeneratorTypeChose(@NonNull GeneratorType type) {

    }

    @Override
    public void showEditGeneratorParams(@NonNull GeneratorType type) {

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
