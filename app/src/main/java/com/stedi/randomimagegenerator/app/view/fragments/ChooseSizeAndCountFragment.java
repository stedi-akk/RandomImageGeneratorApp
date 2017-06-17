package com.stedi.randomimagegenerator.app.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSizeAndCountPresenter;
import com.stedi.randomimagegenerator.app.view.fragments.base.StepFragment;

import javax.inject.Inject;

public class ChooseSizeAndCountFragment extends StepFragment implements ChooseSizeAndCountPresenter.UIImpl {

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
        super.onCreateView(inflater, container, savedInstanceState);
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
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
