package com.stedi.randomimagegenerator.app.view.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.other.Utils;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSaveOptionsPresenter;
import com.stedi.randomimagegenerator.app.view.fragments.base.StepFragment;
import com.stepstone.stepper.VerificationError;

import javax.inject.Inject;

import butterknife.BindView;

public class ChooseSaveOptionsFragment extends StepFragment implements
        RadioGroup.OnCheckedChangeListener,
        TextWatcher,
        ChooseSaveOptionsPresenter.UIImpl {

    @Inject ChooseSaveOptionsPresenter presenter;
    @Inject Logger logger;

    @BindView(R.id.choose_save_options_fragment_rg_format) RadioGroup rgFormat;
    @BindView(R.id.choose_save_options_fragment_et_quality) EditText etQuality;
    @BindView(R.id.choose_save_options_fragment_tv_folder) TextView tvFolder;

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
        return inflater.inflate(R.layout.choose_save_options_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (Bitmap.CompressFormat format : Bitmap.CompressFormat.values()) {
            RadioButton rb = new RadioButton(getContext());
            rb.setText(format.name());
            rb.setId(format.ordinal());
            rgFormat.addView(rb);
        }
        etQuality.addTextChangedListener(this);
        rgFormat.setOnCheckedChangeListener(this);
        if (savedInstanceState == null)
            presenter.getData();
    }

    @Override
    public void afterTextChanged(Editable s) {
        String input = etQuality.getText().toString();
        if (input.isEmpty()) {
            onIncorrectQualityValue();
            return;
        }
        try {
            presenter.setQualityValue(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            logger.log(this, e);
            onIncorrectQualityValue();
        }
    }

    @Override
    public void showQualityFormat(@NonNull Bitmap.CompressFormat format) {
        rgFormat.setOnCheckedChangeListener(null);
        rgFormat.check(format.ordinal());
        rgFormat.setOnCheckedChangeListener(this);
    }

    @Override
    public void showQualityValue(int value) {
        etQuality.removeTextChangedListener(this);
        etQuality.setText(String.valueOf(value));
        etQuality.addTextChangedListener(this);
    }

    @Override
    public void showSaveFolder(@NonNull String path) {
        tvFolder.setText(path);
    }

    @Override
    public void onIncorrectQualityValue() {
        etQuality.setError("incorrect");
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        presenter.setQualityFormat(Bitmap.CompressFormat.values()[checkedId]);
    }

    @Override
    public VerificationError verifyStep() {
        return etQuality.getError() != null ? new VerificationError("you shall not pass!") : null;
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
