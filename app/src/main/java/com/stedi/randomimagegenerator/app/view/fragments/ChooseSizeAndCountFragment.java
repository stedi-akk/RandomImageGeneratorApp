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
import com.stedi.randomimagegenerator.app.other.Utils;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSizeAndCountPresenter;
import com.stedi.randomimagegenerator.app.view.activity.GenerationStepsActivity;
import com.stedi.randomimagegenerator.app.view.fragments.base.StepFragment;
import com.stepstone.stepper.VerificationError;

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
        ((GenerationStepsActivity) getActivity()).getGenerationComponent().inject(this);
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
        if (savedInstanceState == null) {
            presenter.getValues();
        }
    }

    @Override
    public void onSelected() {
        if (getView() != null) {
            etWidth.setSelection(etWidth.getText().length());
            etWidth.requestFocus();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (etWidth.hasFocus()) {
            logger.log(this, "afterTextChanged for etWidth");
            clearSilently(etWidthRangeFrom, etWidthRangeTo, etWidthRangeStep);
            if (!isHeightRangeInEdit()) {
                fillIfEmptySilently(etHeight, etCount);
                presenter.setWidth(1);
                presenter.setHeight(getValue(etHeight));
                presenter.setCount(getValue(etCount));
            }
            if (isEmpty(etWidth) || getValue(etWidth) == 0) {
                presenter.setWidth(1);
                etWidth.setError(getErrorString(ChooseSizeAndCountPresenter.Error.INCORRECT_WIDTH));
                return;
            }
            presenter.setWidth(getValue(etWidth));
        } else if (etHeight.hasFocus()) {
            logger.log(this, "afterTextChanged for etHeight");
            clearSilently(etHeightRangeFrom, etHeightRangeTo, etHeightRangeStep);
            if (!isWidthRangeInEdit()) {
                fillIfEmptySilently(etWidth, etCount);
                presenter.setWidth(getValue(etWidth));
                presenter.setHeight(1);
                presenter.setCount(getValue(etCount));
            }
            if (isEmpty(etHeight) || getValue(etHeight) == 0) {
                presenter.setHeight(1);
                etHeight.setError(getErrorString(ChooseSizeAndCountPresenter.Error.INCORRECT_HEIGHT));
                return;
            }
            presenter.setHeight(getValue(etHeight));
        } else if (etCount.hasFocus()) {
            logger.log(this, "afterTextChanged for etCount");
            clearSilently(etWidthRangeFrom, etWidthRangeTo, etWidthRangeStep, etHeightRangeFrom, etHeightRangeTo, etHeightRangeStep);
            fillIfEmptySilently(etWidth, etHeight);
            presenter.setWidth(getValue(etWidth));
            presenter.setHeight(getValue(etHeight));
            if (isEmpty(etCount) || getValue(etCount) == 0) {
                etCount.setError(getErrorString(ChooseSizeAndCountPresenter.Error.INCORRECT_COUNT));
                return;
            }
            presenter.setCount(getValue(etCount));
        } else if (etWidthRangeFrom.hasFocus()) {
            logger.log(this, "afterTextChanged for etWidthRangeFrom");
            fillIfEmptySilently(etWidthRangeTo, etWidthRangeStep);
            afterWidthRangeTextChanged(etWidthRangeFrom);
        } else if (etWidthRangeTo.hasFocus()) {
            logger.log(this, "afterTextChanged for etWidthRangeTo");
            fillIfEmptySilently(etWidthRangeFrom, etWidthRangeStep);
            afterWidthRangeTextChanged(etWidthRangeTo);
        } else if (etWidthRangeStep.hasFocus()) {
            logger.log(this, "afterTextChanged for etWidthRangeStep");
            fillIfEmptySilently(etWidthRangeFrom, etWidthRangeTo);
            afterWidthRangeTextChanged(etWidthRangeStep);
        } else if (etHeightRangeFrom.hasFocus()) {
            logger.log(this, "afterTextChanged for etHeightRangeFrom");
            fillIfEmptySilently(etHeightRangeTo, etHeightRangeStep);
            afterHeightRangeTextChanged(etHeightRangeFrom);
        } else if (etHeightRangeTo.hasFocus()) {
            logger.log(this, "afterTextChanged for etHeightRangeTo");
            fillIfEmptySilently(etHeightRangeFrom, etHeightRangeStep);
            afterHeightRangeTextChanged(etHeightRangeTo);
        } else if (etHeightRangeStep.hasFocus()) {
            logger.log(this, "afterTextChanged for etHeightRangeStep");
            fillIfEmptySilently(etHeightRangeFrom, etHeightRangeTo);
            afterHeightRangeTextChanged(etHeightRangeStep);
        }
    }

    private void afterWidthRangeTextChanged(EditText etRangeType) {
        clearSilently(etWidth, etCount);
        if (isEmpty(etRangeType) || getValue(etRangeType) == 0) {
            etRangeType.setError(getErrorString(ChooseSizeAndCountPresenter.Error.INCORRECT_WIDTH_RANGE));
            return;
        }
        presenter.setWidthRange(getValue(etWidthRangeFrom), getValue(etWidthRangeTo), getValue(etWidthRangeStep));
    }

    private void afterHeightRangeTextChanged(EditText etRangeType) {
        clearSilently(etHeight, etCount);
        if (isEmpty(etRangeType) || getValue(etRangeType) == 0) {
            etRangeType.setError(getErrorString(ChooseSizeAndCountPresenter.Error.INCORRECT_HEIGHT_RANGE));
            return;
        }
        presenter.setHeightRange(getValue(etHeightRangeFrom), getValue(etHeightRangeTo), getValue(etHeightRangeStep));
    }

    @Override
    public void showWidth(int width) {
        etWidth.setText(String.valueOf(width));
    }

    @Override
    public void showHeight(int height) {
        etHeight.setText(String.valueOf(height));
    }

    @Override
    public void showWidthRange(int from, int to, int step) {
        etWidthRangeFrom.setText(String.valueOf(from));
        etWidthRangeTo.setText(String.valueOf(to));
        etWidthRangeStep.setText(String.valueOf(step));
    }

    @Override
    public void showHeightRange(int from, int to, int step) {
        etHeightRangeFrom.setText(String.valueOf(from));
        etHeightRangeTo.setText(String.valueOf(to));
        etHeightRangeStep.setText(String.valueOf(step));
    }

    @Override
    public void showCount(int count) {
        etCount.setText(String.valueOf(count));
    }

    @Override
    public void onError(@NonNull ChooseSizeAndCountPresenter.Error error) {
        String errorString = getErrorString(error);
        switch (error) {
            case INCORRECT_COUNT:
                etCount.setError(errorString);
                break;
            case INCORRECT_WIDTH:
                etWidth.setError(errorString);
                break;
            case INCORRECT_HEIGHT:
                etHeight.setError(errorString);
                break;
            case INCORRECT_WIDTH_RANGE:
                etWidthRangeFrom.setError(errorString);
                etWidthRangeTo.setError(errorString);
                etWidthRangeStep.setError(errorString);
                break;
            case INCORRECT_HEIGHT_RANGE:
                etHeightRangeFrom.setError(errorString);
                etHeightRangeTo.setError(errorString);
                etHeightRangeStep.setError(errorString);
                break;
            default:
                throw new IllegalStateException("unreachable code");
        }
    }

    @Override
    public VerificationError verifyStep() {
        return hasAnyErrors() ? new VerificationError(getString(R.string.correct_errors)) : null;
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Utils.toastLong(getContext(), error.getErrorMessage());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

    private boolean isEmpty(EditText et) {
        return et.getText().toString().isEmpty();
    }

    private int getValue(EditText et) {
        try {
            return Integer.parseInt(et.getText().toString());
        } catch (NumberFormatException e) {
            logger.log(this, e);
            return 0;
        }
    }

    private void fillIfEmptySilently(EditText... editTexts) {
        for (EditText et : editTexts) {
            String text = et.getText().toString();
            if (!text.isEmpty() && getValue(et) > 0)
                continue;
            et.removeTextChangedListener(this);
            et.setText("1");
            et.setError(null);
            et.addTextChangedListener(this);
        }
    }

    private void clearSilently(EditText... editTexts) {
        for (EditText et : editTexts) {
            et.removeTextChangedListener(this);
            et.setText("");
            et.setError(null);
            et.addTextChangedListener(this);
        }
    }

    private boolean isWidthRangeInEdit() {
        return !etWidthRangeFrom.getText().toString().isEmpty() ||
                !etWidthRangeTo.getText().toString().isEmpty() ||
                !etWidthRangeStep.getText().toString().isEmpty();
    }

    private boolean isHeightRangeInEdit() {
        return !etHeightRangeFrom.getText().toString().isEmpty() ||
                !etHeightRangeTo.getText().toString().isEmpty() ||
                !etHeightRangeStep.getText().toString().isEmpty();
    }

    private boolean hasAnyErrors() {
        return etWidth.getError() != null || etHeight.getError() != null || etCount.getError() != null ||
                etWidthRangeFrom.getError() != null || etWidthRangeTo.getError() != null || etWidthRangeStep.getError() != null ||
                etHeightRangeFrom.getError() != null || etHeightRangeTo.getError() != null || etHeightRangeStep.getError() != null;
    }

    private String getErrorString(ChooseSizeAndCountPresenter.Error error) {
        switch (error) {
            case INCORRECT_WIDTH:
            case INCORRECT_HEIGHT:
            case INCORRECT_WIDTH_RANGE:
            case INCORRECT_HEIGHT_RANGE:
            case INCORRECT_COUNT:
                return getString(R.string.value_bigger_zero);
            default:
                throw new IllegalStateException("unreachable code");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
