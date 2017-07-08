package com.stedi.randomimagegenerator.app.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.other.Utils;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ApplyGenerationPresenter;
import com.stedi.randomimagegenerator.app.view.fragments.base.StepFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ApplyGenerationFragment extends StepFragment implements ApplyGenerationPresenter.UIImpl {
    @Inject ApplyGenerationPresenter presenter;

    @BindView(R.id.apply_generation_fragment_tv) TextView tvOut;
    @BindView(R.id.apply_generation_fragment_btn_save_preset) Button btnSave;

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
        return inflater.inflate(R.layout.apply_generation_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refresh();
    }

    @Override
    public void onSelected() {
        refresh();
    }

    private void refresh() {
        if (getView() != null) {
            tvOut.setText(presenter.getPreset().toString());
            btnSave.setText(presenter.isPresetNewOrChanged() ? "SAVE" : "RENAME");
        }
    }

    @Override
    public void onPresetSaved() {
        getActivity().finish();
    }

    @Override
    public void failedToSavePreset() {
        Utils.toastShort(getContext(), "failedToSavePreset");
    }

    @OnClick(R.id.apply_generation_fragment_btn_save_preset)
    public void onSavePresetClick(View v) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
