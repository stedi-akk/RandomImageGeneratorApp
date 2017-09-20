package com.stedi.randomimagegenerator.app.view.fragments;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.stedi.randomimagegenerator.ImageParams;
import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.Utils;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ApplyGenerationPresenter;
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity;
import com.stedi.randomimagegenerator.app.view.dialogs.EditPresetNameDialog;
import com.stedi.randomimagegenerator.app.view.fragments.base.StepFragment;

import java.io.Serializable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ApplyGenerationFragment extends StepFragment implements ApplyGenerationPresenter.UIImpl {
    private static final int REQUEST_CODE_WRITE_EXTERNAL = 22;
    private static final String KEY_APPLY_GENERATION_PRESENTER_STATE = "KEY_APPLY_GENERATION_PRESENTER_STATE";

    @Inject ApplyGenerationPresenter presenter;
    @Inject CachedBus bus;
    @Inject Logger logger;

    @BindView(R.id.apply_generation_fragment_tv) TextView tvOut;
    @BindView(R.id.apply_generation_fragment_btn_save_preset) Button btnSave;

    private Preset startGenerationPreset;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Components.getGenerationComponent(this).inject(this);
        presenter.onAttach(this);
        if (savedInstanceState != null) {
            Serializable state = savedInstanceState.getSerializable(KEY_APPLY_GENERATION_PRESENTER_STATE);
            if (state != null)
                presenter.onRestore(state);
        }
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
        refreshFromPreset();
    }

    @Override
    public void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @Override
    public void onSelected() {
        refreshFromPreset();
    }

    private void refreshFromPreset() {
        if (getView() != null) {
            tvOut.setText(presenter.getPreset().toString());
            btnSave.setText(presenter.isPresetNewOrChanged() ? "SAVE" : "RENAME");
        }
    }

    @OnClick(R.id.apply_generation_fragment_btn_save_preset)
    public void onSavePresetClick(View v) {
        EditPresetNameDialog.newInstance(presenter.getPreset().getName()).show(getFragmentManager());
    }

    @OnClick(R.id.apply_generation_fragment_btn_generate)
    public void onGenerateClick(View v) {
        if (checkForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_WRITE_EXTERNAL)) {
            presenter.startGeneration(presenter.getPreset());
        } else {
            startGenerationPreset = presenter.getPreset();
        }
    }

    @Subscribe
    public void onEditedPresetName(EditPresetNameDialog.OnEdited onEdited) {
        presenter.savePreset(onEdited.name);
    }

    @Subscribe
    public void onPermissionEvent(BaseActivity.PermissionEvent event) {
        if (event.requestCode == REQUEST_CODE_WRITE_EXTERNAL) {
            if (event.isGranted && startGenerationPreset != null) {
                //noinspection MissingPermission
                presenter.startGeneration(startGenerationPreset);
            }
            startGenerationPreset = null;
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

    @Override
    public void onStartGeneration() {
        logger.log(this, "onStartGeneration");
    }

    @Override
    public void onGenerated(@NonNull ImageParams imageParams) {
        logger.log(this, "onGenerated");
    }

    @Override
    public void onGenerationUnknownError() {
        logger.log(this, "onGenerationUnknownError");
    }

    @Override
    public void onFailedToGenerate(@NonNull ImageParams imageParams) {
        logger.log(this, "onFailedToGenerate");
    }

    @Override
    public void onFinishGeneration() {
        Utils.toastShort(getContext(), "onFinishGeneration");
        getActivity().finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_APPLY_GENERATION_PRESENTER_STATE, presenter.onRetain());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
