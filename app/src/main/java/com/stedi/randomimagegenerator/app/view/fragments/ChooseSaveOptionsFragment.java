package com.stedi.randomimagegenerator.app.view.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.other.CommonKt;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSaveOptionsPresenter;
import com.stedi.randomimagegenerator.app.view.activity.GenerationStepsActivity;
import com.stedi.randomimagegenerator.app.view.fragments.base.StepFragment;

import java.lang.reflect.Field;

import javax.inject.Inject;

import butterknife.BindView;

public class ChooseSaveOptionsFragment extends StepFragment implements
        RadioGroup.OnCheckedChangeListener,
        NumberPicker.OnValueChangeListener,
        ChooseSaveOptionsPresenter.UIImpl {

    private static final String KEY_QUALITY_FORMAT = "KEY_QUALITY_FORMAT";
    private static final String KEY_QUALITY_VALUE = "KEY_QUALITY_VALUE";

    @Inject ChooseSaveOptionsPresenter presenter;
    @Inject Logger logger;

    @BindView(R.id.choose_save_options_fragment_rg_format) RadioGroup rgFormat;
    @BindView(R.id.choose_save_options_fragment_value_picker) NumberPicker npQuality;

    private Bitmap.CompressFormat selectedFormat = null;
    private int selectedValue = -1;

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
        return inflater.inflate(R.layout.choose_save_options_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addFormatButtons();
        npQuality.setMinValue(0);
        npQuality.setMaxValue(100);
        npQuality.setOnValueChangedListener(this);
        setDividerColor(npQuality, getResources().getColor(R.color.colorAccent));
        rgFormat.setOnCheckedChangeListener(this);
        if (savedInstanceState == null) {
            presenter.getData();
        } else {
            selectedFormat = (Bitmap.CompressFormat) savedInstanceState.getSerializable(KEY_QUALITY_FORMAT);
            selectedValue = savedInstanceState.getInt(KEY_QUALITY_VALUE);
            refresh();
        }
    }

    private void addFormatButtons() {
        int height = (int) CommonKt.dp2px(getActivity(), 48f);
        for (Bitmap.CompressFormat format : Bitmap.CompressFormat.values()) {
            RadioButton rb = new AppCompatRadioButton(getActivity());
            rb.setId(format.ordinal());
            rb.setText(format.name());
            rb.setMinHeight(height);
            rb.setGravity(Gravity.CENTER);
            rgFormat.addView(rb);
        }
    }

    @Override
    public void onSelected() {
        if (getView() == null)
            return;
        refresh();
    }

    private void refresh() {
        if (selectedFormat == null || selectedValue == -1)
            return;
        rgFormat.setOnCheckedChangeListener(null);
        rgFormat.clearCheck();
        rgFormat.check(selectedFormat.ordinal());
        rgFormat.setOnCheckedChangeListener(this);
        npQuality.setOnValueChangedListener(null);
        npQuality.setValue(selectedValue);
        npQuality.setOnValueChangedListener(this);
    }

    @Override
    public void showQualityFormat(@NonNull Bitmap.CompressFormat format) {
        selectedFormat = format;
    }

    @Override
    public void showQualityValue(int value) {
        selectedValue = value;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        selectedFormat = Bitmap.CompressFormat.values()[checkedId];
        presenter.setQualityFormat(selectedFormat);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        selectedValue = newVal;
        presenter.setQualityValue(selectedValue);
    }

    @Override
    public void onIncorrectQualityValue() {
        logger.log(this, "onIncorrectQualityValue");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_QUALITY_FORMAT, selectedFormat);
        outState.putInt(KEY_QUALITY_VALUE, selectedValue);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

    // https://stackoverflow.com/questions/24233556/changing-numberpicker-divider-color
    private void setDividerColor(@NonNull NumberPicker picker, int color) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    pf.set(picker, new ColorDrawable(color));
                } catch (Exception e) {
                    // ignore
                }
                break;
            }
        }
    }
}
