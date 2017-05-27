package com.stedi.randomimagegenerator.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.modules.HomeModule;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.Utils;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.HomePresenter;
import com.stedi.randomimagegenerator.app.view.adapters.PresetsAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements HomePresenter.UIImpl, PresetsAdapter.ClickListener {
    @Inject HomePresenter presenter;
    @Inject Logger logger;

    @BindView(R.id.home_activity_recycler_view) RecyclerView recyclerView;

    private PresetsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().plus(new HomeModule()).inject(this);
        presenter.onAttach(this);
        setContentView(R.layout.home_activity);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PresetsAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        logger.log(this, "fetchPresets");
        presenter.fetchPresets();
    }

    @Override
    public void onPresetsFetched(@Nullable Preset pendingPreset, @NonNull List<Preset> presets) {
        logger.log(this, "onPresetsFetched");
        adapter.addAll(presets);
        adapter.setPendingPreset(pendingPreset);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFailedToFetchPresets() {
        Utils.toastLong(this, "onFailedToFetch");
    }

    @Override
    public void onLongRunningAction(boolean stopped) {

    }

    @Override
    public void onFailedToDeletePreset() {

    }

    @Override
    public void showConfirmLastAction() {

    }

    @OnClick(R.id.home_activity_fab)
    public void onFabClick(View v) {
        presenter.createNewGeneration();
    }

    @Override
    public void showNewGeneration() {
        startActivity(new Intent(this, GenerationActivity.class));
    }

    @Override
    public void showEditPreset() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

    @Override
    public void onCardClick(@NonNull Preset preset) {

    }

    @Override
    public void onDeleteClick(@NonNull Preset preset) {

    }

    @Override
    public void onOpenFolderClick(@NonNull Preset preset) {

    }

    @Override
    public void onGenerateClick(@NonNull Preset preset) {

    }
}
