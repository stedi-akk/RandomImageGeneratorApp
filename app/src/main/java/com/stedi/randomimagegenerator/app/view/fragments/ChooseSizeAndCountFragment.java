package com.stedi.randomimagegenerator.app.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSizeAndCountPresenter;
import com.stedi.randomimagegenerator.app.view.fragments.base.StepFragment;

import javax.inject.Inject;

import butterknife.BindView;

public class ChooseSizeAndCountFragment extends StepFragment implements
        TextWatcher,
        ChooseSizeAndCountPresenter.UIImpl {

    @Inject ChooseSizeAndCountPresenter presenter;
    @Inject Logger logger;

    @BindView(R.id.choose_size_and_count_et_width) EditText etWidth;
    @BindView(R.id.choose_size_and_count_et_height) EditText etHeight;
    @BindView(R.id.choose_size_and_count_et_count) EditText etCount;
    @BindView(R.id.choose_size_and_count_et_width_range_from) EditText etWidthRangeFrom;
    @BindView(R.id.choose_size_and_count_et_width_range_to) EditText etWidthRangeTo;
    @BindView(R.id.choose_size_and_count_et_width_range_step) EditText etWidthRangeStep;
    @BindView(R.id.choose_size_and_count_et_height_range_from) EditText etHeightRangeFrom;
    @BindView(R.id.choose_size_and_count_et_height_range_to) EditText etHeightRangeTo;
    @BindView(R.id.choose_size_and_count_et_height_range_step) EditText etHeightRangeStep;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etWidth.addTextChangedListener(this);
        etHeight.addTextChangedListener(this);
        etCount.addTextChangedListener(this);
        etWidthRangeFrom.addTextChangedListener(this);
        etWidthRangeTo.addTextChangedListener(this);
        etWidthRangeStep.addTextChangedListener(this);
        etHeightRangeFrom.addTextChangedListener(this);
        etHeightRangeTo.addTextChangedListener(this);
        etHeightRangeStep.addTextChangedListener(this);
        if (savedInstanceState == null)
            presenter.getValues();
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (etWidth.hasFocus()) {

        } else if (etHeight.hasFocus()) {

        } else if (etCount.hasFocus()) {

        } else if (etWidthRangeFrom.hasFocus()) {

        } else if (etWidthRangeTo.hasFocus()) {

        } else if (etWidthRangeStep.hasFocus()) {

        } else if (etHeightRangeFrom.hasFocus()) {

        } else if (etHeightRangeTo.hasFocus()) {

        } else if (etHeightRangeStep.hasFocus()) {

        }
    }

    @Override
    public void showWidth(int width) {

    }

    @Override
    public void showHeight(int height) {

    }

    @Override
    public void showWidthRange(int from, int to, int step) {

    }

    @Override
    public void showHeightRange(int from, int to, int step) {

    }

    @Override
    public void showCount(int count) {

    }

    @Override
    public void onError(@NonNull ChooseSizeAndCountPresenter.Error error) {
        switch (error) {
            case INCORRECT_COUNT:
                etCount.setError(error.name());
                break;
            case INCORRECT_WIDTH:
                etWidth.setError(error.name());
                break;
            case INCORRECT_HEIGHT:
                etHeight.setError(error.name());
                break;
            case INCORRECT_WIDTH_RANGE:
                etWidthRangeFrom.setError(error.name());
                etWidthRangeTo.setError(error.name());
                etWidthRangeStep.setError(error.name());
                break;
            case INCORRECT_HEIGHT_RANGE:
                etHeightRangeFrom.setError(error.name());
                etHeightRangeTo.setError(error.name());
                etHeightRangeStep.setError(error.name());
                break;
            default:
                throw new IllegalStateException("unreachable code");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
