package com.stedi.randomimagegenerator.app.view.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.SimpleIntegerParamsPresenter;
import com.stedi.randomimagegenerator.app.view.dialogs.base.ButterKnifeDialogFragment;

import javax.inject.Inject;

import butterknife.BindView;

public class SimpleIntegerParamsDialog extends ButterKnifeDialogFragment implements
        TextWatcher,
        CompoundButton.OnCheckedChangeListener,
        SimpleIntegerParamsPresenter.UIImpl {

    private static final GeneratorType[] supportedTypes = new GeneratorType[]{
            GeneratorType.COLORED_CIRCLES,
            GeneratorType.COLORED_RECTANGLE,
            GeneratorType.COLORED_PIXELS
    };

    private static final String KEY_TYPE = "KEY_TYPE";

    @Inject SimpleIntegerParamsPresenter presenter;
    @Inject Logger logger;

    @BindView(R.id.simple_integer_params_tv_input) TextView tvInput;
    @BindView(R.id.simple_integer_params_dialog_et_value) EditText etValue;
    @BindView(R.id.simple_integer_params_dialog_cb_random) CheckBox cbRandom;

    private boolean canBeRandom;

    public static SimpleIntegerParamsDialog newInstance(@NonNull GeneratorType type) {
        boolean supported = false;
        for (GeneratorType supportedType : supportedTypes) {
            if (type == supportedType) {
                supported = true;
                break;
            }
        }

        if (!supported)
            throw new IllegalArgumentException(type.name() + " is not supported");

        Bundle args = new Bundle();
        args.putSerializable(KEY_TYPE, type);
        SimpleIntegerParamsDialog dlg = new SimpleIntegerParamsDialog();
        dlg.setArguments(args);
        return dlg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Components.getGenerationComponent(this).inject(this);
        presenter.onAttach(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        GeneratorType type = (GeneratorType) getArguments().getSerializable(KEY_TYPE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton(R.string.ok, null);
        //noinspection ConstantConditions
        builder.setTitle(getString(R.string.s_parameters, getString(type.getStringRes())));
        builder.setView(inflateAndBind(R.layout.simple_integer_params_dialog));

        switch (type) {
            case COLORED_CIRCLES:
                tvInput.setText(R.string.circles_count);
                break;
            case COLORED_RECTANGLE:
                tvInput.setText(R.string.rectangles_count);
                break;
            case COLORED_PIXELS:
                tvInput.setText(R.string.pixel_multiplier);
        }

        canBeRandom = presenter.canBeRandom();
        if (canBeRandom) {
            etValue.addTextChangedListener(this);
            cbRandom.setOnCheckedChangeListener(this);
        } else {
            cbRandom.setVisibility(View.GONE);
        }

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialog1 -> {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> apply());
        });

        if (savedInstanceState == null)
            presenter.getValues();
        return dialog;
    }

    @Override
    public void afterTextChanged(Editable s) {
        logger.log(this, "afterTextChanged");
        setRandomCheckSilently(false);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        logger.log(this, "onChecked " + isChecked);
        setValueTextSilently("");
    }

    @Override
    public void showRandomValue() {
        logger.log(this, "showRandomValue");
        setValueTextSilently("");
        setRandomCheckSilently(true);
    }

    @Override
    public void showValue(int value) {
        logger.log(this, "showValue " + value);
        setValueTextSilently(String.valueOf(value));
        if (canBeRandom)
            setRandomCheckSilently(false);
    }

    @Override
    public void showErrorIncorrectValue() {
        etValue.setError(getString(R.string.value_bigger_zero));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

    private void apply() {
        boolean success;
        if (canBeRandom && cbRandom.isChecked()) {
            presenter.setRandomValue();
            success = true;
        } else {
            String input = etValue.getText().toString();
            if (input.isEmpty()) {
                showErrorIncorrectValue();
                return;
            }
            try {
                success = presenter.setValue(Integer.parseInt(input));
            } catch (NumberFormatException e) {
                logger.log(this, e);
                showErrorIncorrectValue();
                return;
            }
        }
        if (success)
            dismiss();
    }

    private void setValueTextSilently(String text) {
        if (canBeRandom)
            etValue.removeTextChangedListener(this);
        etValue.setText(text);
        etValue.setSelection(text.length());
        etValue.setError(null);
        if (canBeRandom)
            etValue.addTextChangedListener(this);
    }

    private void setRandomCheckSilently(boolean check) {
        cbRandom.setOnCheckedChangeListener(null);
        cbRandom.setChecked(check);
        cbRandom.setOnCheckedChangeListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
