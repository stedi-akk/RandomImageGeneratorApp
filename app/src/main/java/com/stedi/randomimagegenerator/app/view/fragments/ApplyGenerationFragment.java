package com.stedi.randomimagegenerator.app.view.fragments;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.stedi.randomimagegenerator.ImageParams;
import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.RootSavePath;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.CommonKt;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ApplyGenerationPresenter;
import com.stedi.randomimagegenerator.app.view.activity.GenerationStepsActivity;
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity;
import com.stedi.randomimagegenerator.app.view.dialogs.EditPresetNameDialog;
import com.stedi.randomimagegenerator.app.view.dialogs.GenerationDialog;
import com.stedi.randomimagegenerator.app.view.fragments.base.StepFragment;

import java.io.File;
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
    @Inject @RootSavePath String rootSavePath;

    @BindView(R.id.apply_generation_fragment_tv) TextView tvOut;
    @BindView(R.id.apply_generation_fragment_btn_save) Button btnSave;

    private Preset startGenerationPreset;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GenerationStepsActivity) getActivity()).getGenerationComponent().inject(this);
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
            tvOut.setText(getSummaryFromPreset(presenter.getPreset()));
            btnSave.setText(presenter.isPresetNewOrChanged() ? R.string.unsaved_save : R.string.save);
        }
    }

    private String getSummaryFromPreset(Preset preset) {
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.name_s, preset.getName()));
        sb.append("\n\n");
        if (preset.getTimestamp() != 0) {
            sb.append(getString(R.string.created_s, CommonKt.formatTime(preset.getTimestamp())));
            sb.append("\n\n");
        }

        if (preset.getGeneratorParams() instanceof EffectGeneratorParams) {
            GeneratorParams targetParams = ((EffectGeneratorParams) preset.getGeneratorParams()).getTarget();
            sb.append(getString(R.string.generator_type_s, getString(targetParams.getType().getNameRes())));
            sb.append("\n\n");
            sb.append(getString(R.string.effect_type_s, getString(preset.getGeneratorParams().getType().getNameRes())));
            sb.append("\n\n");
        } else {
            sb.append(getString(R.string.generator_type_s, getString(preset.getGeneratorParams().getType().getNameRes())));
            sb.append("\n\n");
        }

        boolean showCount = true;
        if (appendRangeSize(sb, R.string.width_s, preset.getWidthRange())) {
            showCount = false;
        } else {
            sb.append(getString(R.string.width_s, String.valueOf(preset.getWidth())));
        }
        sb.append("\n\n");
        if (appendRangeSize(sb, R.string.height_s, preset.getHeightRange())) {
            showCount = false;
        } else {
            sb.append(getString(R.string.height_s, String.valueOf(preset.getHeight())));
        }
        sb.append("\n\n");
        if (showCount) {
            sb.append(getString(R.string.count_s, String.valueOf(preset.getCount())));
            sb.append("\n\n");
        }

        sb.append(getString(R.string.quality_s_percent, preset.getQuality().getFormat().name(), String.valueOf(preset.getQuality().getQualityValue())));
        sb.append("\n\n");
        sb.append(getString(R.string.save_folder_s, CommonKt.formatSavePath(rootSavePath, preset.getPathToSave())));

        return sb.toString();
    }

    private boolean appendRangeSize(StringBuilder sb, @StringRes int res, int[] size) {
        if (size == null)
            return false;
        String from = String.valueOf(size[0]);
        String to = String.valueOf(size[1]);
        String step = String.valueOf(size[2]);
        sb.append(getString(res, getString(R.string.from_s_to_s_step_s, from, to, step)));
        return true;
    }

    @OnClick(R.id.apply_generation_fragment_btn_save)
    public void onSaveClick(View v) {
        Preset preset = presenter.getPreset();
        EditPresetNameDialog.newInstance(preset.getId() == 0 ? "" : preset.getName()).show(getFragmentManager());
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
        if (event.getRequestCode() == REQUEST_CODE_WRITE_EXTERNAL) {
            if (event.isGranted() && startGenerationPreset != null) {
                //noinspection MissingPermission
                presenter.startGeneration(startGenerationPreset);
            }
            startGenerationPreset = null;
        }
    }

    @Subscribe
    public void onOkClickedEvent(GenerationDialog.OkClicked event) {
        getActivity().finish();
    }

    @Override
    public void onPresetSaved() {
        getActivity().finish();
    }

    @Override
    public void failedToSavePreset() {
        CommonKt.showToast(getContext(), R.string.failed_save_preset, Toast.LENGTH_LONG);
    }

    @Override
    public void onStartGeneration() {
        logger.log(this, "onStartGeneration");
        GenerationDialog.getInstance(getFragmentManager()).onStartGeneration();
    }

    @Override
    public void onGenerated(@NonNull ImageParams imageParams, @NonNull File imageFile) {
        logger.log(this, "onGenerated");
        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)));
        GenerationDialog.getInstance(getFragmentManager()).onGenerated(imageParams, imageFile);
    }

    @Override
    public void onGenerationUnknownError() {
        logger.log(this, "onGenerationUnknownError");
        GenerationDialog.getInstance(getFragmentManager()).onGenerationUnknownError();
    }

    @Override
    public void onFailedToGenerate(@NonNull ImageParams imageParams) {
        logger.log(this, "onFailedToGenerate");
        GenerationDialog.getInstance(getFragmentManager()).onFailedToGenerate(imageParams);
    }

    @Override
    public void onFinishGeneration() {
        logger.log(this, "onFinishGeneration");
        GenerationDialog.getInstance(getFragmentManager()).onFinishGeneration();
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
