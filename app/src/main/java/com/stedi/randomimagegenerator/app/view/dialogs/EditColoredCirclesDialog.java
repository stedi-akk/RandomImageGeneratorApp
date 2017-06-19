package com.stedi.randomimagegenerator.app.view.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.other.Utils;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.EditColoredCirclesPresenter;
import com.stedi.randomimagegenerator.app.view.dialogs.base.ButterKnifeDialogFragment;

import javax.inject.Inject;

import butterknife.BindView;

public class EditColoredCirclesDialog extends ButterKnifeDialogFragment implements
        TextWatcher,
        CompoundButton.OnCheckedChangeListener,
        EditColoredCirclesPresenter.UIImpl {

    @Inject EditColoredCirclesPresenter presenter;
    @Inject Logger logger;

    @BindView(R.id.edit_colored_circles_dialog_et_count) EditText etCount;
    @BindView(R.id.edit_colored_circles_dialog_cb_random) CheckBox cbRandom;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Components.getGenerationComponent(this).inject(this);
        presenter.onAttach(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton("OK", (dialog, which) -> apply());
        builder.setTitle("Edit colored circles");
        builder.setView(inflateAndBind(R.layout.edit_colored_circles_dialog));
        etCount.addTextChangedListener(this);
        cbRandom.setOnCheckedChangeListener(this);
        if (savedInstanceState == null)
            presenter.getValues();
        return builder.create();
    }

    @Override
    public void afterTextChanged(Editable s) {
        logger.log(this, "afterTextChanged");
        setRandomCheckSilently(false);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        logger.log(this, "onChecked " + isChecked);
        setCountTextSilently("");
    }

    @Override
    public void showRandomCount() {
        logger.log(this, "showRandomCount");
        setCountTextSilently("");
        setRandomCheckSilently(true);
    }

    @Override
    public void showCount(int count) {
        logger.log(this, "showCount " + count);
        setCountTextSilently(String.valueOf(count));
        setRandomCheckSilently(false);
    }

    @Override
    public void showErrorIncorrectCount() {
        Utils.toastLong(getContext(), "count must be > 0");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

    private void apply() {
        if (cbRandom.isChecked()) {
            presenter.setRandomCount();
        } else {
            try {
                int count = Integer.parseInt(etCount.getText().toString());
                presenter.setCount(count);
            } catch (NumberFormatException e) {
                logger.log(this, e);
                showErrorIncorrectCount();
            }
        }
    }

    private void setCountTextSilently(String text) {
        etCount.removeTextChangedListener(this);
        etCount.setText(text);
        etCount.addTextChangedListener(this);
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
