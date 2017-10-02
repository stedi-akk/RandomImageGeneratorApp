package com.stedi.randomimagegenerator.app.view.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.other.Utils;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSaveOptionsPresenter;
import com.stedi.randomimagegenerator.app.view.fragments.base.StepFragment;

import javax.inject.Inject;

import butterknife.BindView;

public class ChooseSaveOptionsFragment extends StepFragment implements
        RadioGroup.OnCheckedChangeListener,
        NumberPicker.OnValueChangeListener,
        ChooseSaveOptionsPresenter.UIImpl {

    private static final String KEY_PICKER_VALUE = "KEY_PICKER_VALUE";

    @Inject ChooseSaveOptionsPresenter presenter;
    @Inject Logger logger;

    @BindView(R.id.choose_save_options_fragment_rg_format) RadioGroup rgFormat;
    @BindView(R.id.choose_save_options_fragment_value_picker) NumberPicker npQuality;

    private Bitmap.CompressFormat selectedFormat;

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
        addFormatButtons();
        npQuality.setMinValue(0);
        npQuality.setMaxValue(100);
        npQuality.setOnValueChangedListener(this);
        rgFormat.setOnCheckedChangeListener(this);
        if (savedInstanceState == null) {
            presenter.getData();
        } else if (savedInstanceState.containsKey(KEY_PICKER_VALUE)) {
            showQualityValue(savedInstanceState.getInt(KEY_PICKER_VALUE));
        }
    }

    private void addFormatButtons() {
        int height = Utils.dp2pxi(getActivity(), 48f);
        for (Bitmap.CompressFormat format : Bitmap.CompressFormat.values()) {
            RadioButton rb = new RadioButton(getActivity());
            rb.setId(format.ordinal());
            rb.setText(format.name());
            rb.setMinHeight(height);
            rb.setGravity(Gravity.CENTER);
            rgFormat.addView(rb);
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        presenter.setQualityValue(newVal);
    }

    @Override
    public void onSelected() {
        if (getView() != null && selectedFormat != null) {
            rgFormat.setOnCheckedChangeListener(null);
            rgFormat.check(selectedFormat.ordinal());
            rgFormat.setOnCheckedChangeListener(this);
            selectedFormat = null;
        }
    }

    @Override
    public void showQualityFormat(@NonNull Bitmap.CompressFormat format) {
        selectedFormat = format;
    }

    @Override
    public void showQualityValue(int value) {
        npQuality.setOnValueChangedListener(null);
        npQuality.setValue(value);
        npQuality.setOnValueChangedListener(this);
    }

    @Override
    public void onIncorrectQualityValue() {
        logger.log(this, "onIncorrectQualityValue");
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        presenter.setQualityFormat(Bitmap.CompressFormat.values()[checkedId]);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (npQuality != null) {
            outState.putInt(KEY_PICKER_VALUE, npQuality.getValue());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
