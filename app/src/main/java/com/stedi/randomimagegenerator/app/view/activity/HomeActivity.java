package com.stedi.randomimagegenerator.app.view.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stedi.randomimagegenerator.ImageParams;
import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.modules.HomeModule;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.Utils;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.HomePresenter;
import com.stedi.randomimagegenerator.app.view.adapters.PresetsAdapter;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements HomePresenter.UIImpl, PresetsAdapter.ClickListener {
    private static final String KEY_HOME_PRESENTER_STATE = "KEY_HOME_PRESENTER_STATE";
    private static final int REQUEST_CODE_WRITE_EXTERNAL = 11;

    @Inject HomePresenter presenter;
    @Inject Logger logger;

    @BindView(R.id.home_activity_recycler_view) RecyclerView recyclerView;

    private PresetsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getActivityComponent().plus(new HomeModule()).inject(this);
        super.onCreate(savedInstanceState);
        presenter.onAttach(this);

        setContentView(R.layout.home_activity);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            Serializable state = savedInstanceState.getSerializable(KEY_HOME_PRESENTER_STATE);
            if (state != null)
                presenter.onRestore(state);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new PresetsAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.fetchPresets();
    }

    @Override
    public void onPresetsFetched(@Nullable Preset pendingPreset, @NonNull List<Preset> presets) {
        logger.log(this, "onPresetsFetched");
        adapter.set(presets);
        adapter.setPendingPreset(pendingPreset);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFailedToFetchPresets() {
        Utils.toastLong(this, "onFailedToFetch");
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
    public void onDeleteClick(@NonNull Preset preset) {

    }

    @Override
    public void onFailedToDeletePreset() {

    }

    @Override
    public void onGenerateClick(@NonNull Preset preset) {
        if (checkForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_WRITE_EXTERNAL))
            presenter.startGeneration(preset);
    }

    @Override
    public void showConfirmLastAction() {

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
        Utils.toastShort(this, getClass().getSimpleName() + " onFinishGeneration");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_HOME_PRESENTER_STATE, presenter.onRetain());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
