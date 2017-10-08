package com.stedi.randomimagegenerator.app.view.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.squareup.otto.Subscribe;
import com.stedi.randomimagegenerator.ImageParams;
import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.modules.HomeModule;
import com.stedi.randomimagegenerator.app.di.qualifiers.RootSavePath;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.Utils;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.HomePresenter;
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity;
import com.stedi.randomimagegenerator.app.view.adapters.PresetsAdapter;
import com.stedi.randomimagegenerator.app.view.components.GeneratorTypeImageLoader;
import com.stedi.randomimagegenerator.app.view.components.ListSpaceDecoration;
import com.stedi.randomimagegenerator.app.view.dialogs.ConfirmDialog;
import com.stedi.randomimagegenerator.app.view.dialogs.GenerationDialog;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements HomePresenter.UIImpl, PresetsAdapter.ClickListener {
    private static final String KEY_HOME_PRESENTER_STATE = "KEY_HOME_PRESENTER_STATE";
    private static final int REQUEST_CODE_WRITE_EXTERNAL = 123;
    private static final int REQUEST_CONFIRM_DELETE = 321;
    private static final int REQUEST_CONFIRM_GENERATE = 231;

    @Inject HomePresenter presenter;
    @Inject Logger logger;
    @Inject CachedBus bus;
    @Inject GeneratorTypeImageLoader adapterImageLoader;
    @Inject @RootSavePath String rootSavePath;

    @BindView(R.id.home_activity_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.home_activity_empty_view) View emptyView;
    @BindView(R.id.home_activity_fab) FloatingActionButton fab;

    private PresetsAdapter adapter;
    private Preset startGenerationPreset;
    private String confirmPresetName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getActivityComponent().plus(new HomeModule()).inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_activity);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            Serializable state = savedInstanceState.getSerializable(KEY_HOME_PRESENTER_STATE);
            if (state != null)
                presenter.onRestore(state);
        }

        fab.hide(fabShowHideListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new ListSpaceDecoration(
                Utils.dp2pxi(this, R.dimen.common_v_spacing), Utils.dp2pxi(this, R.dimen.common_lr_spacing)));
        adapter = new PresetsAdapter(adapterImageLoader, rootSavePath, this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(recyclerScrollListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onAttach(this);
        bus.register(this);
        presenter.fetchPresets();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bus.unregister(this);
        presenter.onDetach();
    }

    @Override
    public void onPresetsFetched(@Nullable Preset pendingPreset, @NonNull List<Preset> presets) {
        fab.show(fabShowHideListener);
        adapter.set(presets, pendingPreset);
        refreshEmptyView();
    }

    @Override
    public void onFailedToFetchPresets() {
        fab.show(fabShowHideListener);
        Utils.toastLong(this, R.string.failed_fetch_presets);
        refreshEmptyView();
    }

    @OnClick(R.id.home_activity_fab)
    public void onFabClick(View v) {
        GenerationStepsActivity.startActivity(this, true);
    }

    @Override
    public void onCardClick(@NonNull Preset preset) {
        presenter.editPreset(preset);
    }

    @Override
    public void showEditPreset() {
        GenerationStepsActivity.startActivity(this, false);
    }

    @Override
    public void onSaveClick(@NonNull Preset preset) {
        presenter.editPreset(preset);
    }

    @Override
    public void onDeleteClick(@NonNull Preset preset) {
        confirmPresetName = preset.getName();
        presenter.deletePreset(preset);
    }

    @Override
    public void onPresetDeleted(@NonNull Preset preset) {
        logger.log(this, "onPresetDeleted " + preset);
        adapter.remove(preset);
        refreshEmptyView();
    }

    @Override
    public void onFailedToDeletePreset() {
        Utils.toastLong(this, R.string.failed_delete_preset);
    }

    @Override
    public void onGenerateClick(@NonNull Preset preset) {
        if (checkForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_WRITE_EXTERNAL)) {
            confirmPresetName = preset.getName();
            presenter.startGeneration(preset);
        } else {
            startGenerationPreset = preset;
        }
    }

    @Override
    public void showConfirmLastAction(@NonNull HomePresenter.Confirm confirm) {
        if (confirm == HomePresenter.Confirm.DELETE_PRESET) {
            ConfirmDialog.newInstance(REQUEST_CONFIRM_DELETE,
                    getString(R.string.confirm_action), getString(R.string.are_you_sure_delete_s_preset, confirmPresetName))
                    .show(getSupportFragmentManager(), ConfirmDialog.class.getSimpleName());
        } else if (confirm == HomePresenter.Confirm.GENERATE_FROM_PRESET) {
            ConfirmDialog.newInstance(REQUEST_CONFIRM_GENERATE,
                    getString(R.string.confirm_action), getString(R.string.are_you_sure_generate_preset_s, confirmPresetName))
                    .show(getSupportFragmentManager(), ConfirmDialog.class.getSimpleName());
        }
    }

    @Subscribe
    public void onPermissionEvent(PermissionEvent event) {
        if (event.requestCode == REQUEST_CODE_WRITE_EXTERNAL) {
            if (event.isGranted && startGenerationPreset != null) {
                confirmPresetName = startGenerationPreset.getName();
                //noinspection MissingPermission
                presenter.startGeneration(startGenerationPreset);
            }
            startGenerationPreset = null;
        }
    }

    @Subscribe
    public void onConfirmDialogCallback(ConfirmDialog.Callback callback) {
        logger.log(this, "onConfirmDialogCallback " + callback.confirm);
        if (callback.requestCode == REQUEST_CONFIRM_DELETE ||
                callback.requestCode == REQUEST_CONFIRM_GENERATE) {
            if (callback.confirm) {
                presenter.confirmLastAction();
            } else {
                presenter.cancelLastAction();
            }
        }
    }

    @Override
    public void onStartGeneration() {
        logger.log(this, "onStartGeneration");
        GenerationDialog.getInstance(getSupportFragmentManager()).onStartGeneration();
    }

    @Override
    public void onGenerated(@NonNull ImageParams imageParams, @NonNull File imageFile) {
        logger.log(this, "onGenerated");
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)));
        GenerationDialog.getInstance(getSupportFragmentManager()).onGenerated(imageParams, imageFile);
    }

    @Override
    public void onGenerationUnknownError() {
        logger.log(this, "onGenerationUnknownError");
        GenerationDialog.getInstance(getSupportFragmentManager()).onGenerationUnknownError();
    }

    @Override
    public void onFailedToGenerate(@NonNull ImageParams imageParams) {
        logger.log(this, "onFailedToGenerate");
        GenerationDialog.getInstance(getSupportFragmentManager()).onFailedToGenerate(imageParams);
    }

    @Override
    public void onFinishGeneration() {
        logger.log(this, "onFinishGeneration");
        GenerationDialog.getInstance(getSupportFragmentManager()).onFinishGeneration();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_HOME_PRESENTER_STATE, presenter.onRetain());
    }

    private void refreshEmptyView() {
        emptyView.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    private RecyclerView.OnScrollListener recyclerScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dy > 0) {
                fab.hide(fabShowHideListener);
            } else {
                fab.show(fabShowHideListener);
            }
        }
    };

    private FloatingActionButton.OnVisibilityChangedListener fabShowHideListener = new FloatingActionButton.OnVisibilityChangedListener() {
        @Override
        public void onShown(FloatingActionButton fab) {
            fab.setVisibility(View.VISIBLE);
        }

        @Override
        public void onHidden(FloatingActionButton fab) {
            fab.setVisibility(View.GONE);
        }
    };
}
