package com.stedi.randomimagegenerator.app.view.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSaveOptionsPresenter;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import javax.inject.Inject;

import butterknife.BindView;

public class ChooseSaveOptionsFragment extends ButterKnifeFragment implements Step, ChooseSaveOptionsPresenter.UIImpl {
    @Inject ChooseSaveOptionsPresenter presenter;

    @BindView(R.id.choose_save_options_fragment_rg) RadioGroup radioGroup;
    @BindView(R.id.choose_save_options_fragment_et_quality) EditText etQuality;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Components.getGenerationComponent(this).inject(this);
        presenter.onAttach(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_save_options_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getFormatsToChoose();
        presenter.getValuesToChoose();
        presenter.getSaveFolder();
    }

    @Override
    public void showQualityFormats(@NonNull Bitmap.CompressFormat[] formats) {
        for (Bitmap.CompressFormat format : formats) {
            RadioButton rb = new RadioButton(getContext());
            rb.setText(format.name());
            radioGroup.addView(rb);
        }
    }

    @Override
    public void showQualityValueRange(@NonNull int[] minMax) {

    }

    @Override
    public void showSaveFolder(@NonNull String path) {

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
