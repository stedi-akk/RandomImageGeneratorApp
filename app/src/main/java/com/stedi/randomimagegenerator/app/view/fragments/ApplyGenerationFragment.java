package com.stedi.randomimagegenerator.app.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.other.Utils;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ApplyGenerationPresenter;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ApplyGenerationFragment extends ButterKnifeFragment implements Step, ApplyGenerationPresenter.UIImpl {
    @Inject ApplyGenerationPresenter presenter;

    @BindView(R.id.apply_generation_fragment_tv) TextView tvOut;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Components.getGenerationComponent(this).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.apply_generation_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onAttach(this);
    }

    @Override
    public void showGeneratorType(@NonNull GeneratorType type) {
        tvOut.append(type.name());
    }

    @Override
    public void showEffectType(@NonNull GeneratorType effectType) {
        tvOut.append("\n" + effectType.name());
    }

    @Override
    public void showQuality(@NonNull Quality quality) {
        tvOut.append("\n" + quality.getFormat().toString() + " " + quality.getQualityValue());
    }

    @Override
    public void showSize(int width, int height) {
        tvOut.append("\n" + width + " " + height);
    }

    @Override
    public void showCount(int count) {
        tvOut.append("\n" + count);
    }

    @Override
    public void finishGeneration() {
        getActivity().finish();
    }

    @Override
    public void failedToSavePreset() {
        Utils.toastShort(getContext(), "failedToSavePreset");
    }

    @OnClick(R.id.apply_generation_fragment_btn_save_preset)
    public void onSavePresetClick(View v) {
        presenter.savePreset();
    }

    @OnClick(R.id.apply_generation_fragment_btn_generate)
    public void onGenerateClick(View v) {
        presenter.startGeneration();
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
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDetach();
    }
}
