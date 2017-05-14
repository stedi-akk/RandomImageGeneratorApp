package com.stedi.randomimagegenerator.app.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.presenter.ChooseSizeAndCountPresenter;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import javax.inject.Inject;

public class ChooseSizeAndCountFragment extends ButterKnifeFragment implements
        ChooseSizeAndCountPresenter.UIImpl,
        Step {

    @Inject ChooseSizeAndCountPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Components.getGenerationComponent(this).inject(this);
        presenter.onAttach(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_size_and_count_fragment, container, false);
    }

    @Override
    public void showCount(int count) {

    }

    @Override
    public void showSize(int width, int height) {

    }

    @Override
    public void showErrorIncorrectCount() {

    }

    @Override
    public void showErrorIncorrectSize() {

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
