package com.stedi.randomimagegenerator.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.modules.HomeModule;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.Utils;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.HomePresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements HomePresenter.UIImpl {
    @Inject HomePresenter presenter;
    @Inject Logger logger;

    @BindView(R.id.home_activity_recycler_view) RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().plus(new HomeModule()).inject(this);
        presenter.onAttach(this);
        setContentView(R.layout.home_activity);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.fetchPresets();
    }

    @Override
    public void onPresetsFetched(@NonNull PendingPreset pendingPreset, @NonNull List<Preset> presets) {
    }

    @Override
    public void onFailedToFetch(@NonNull Throwable t) {
        logger.log(this, t);
        Utils.toast(this, "onFailedToFetch");
    }

    @OnClick(R.id.home_activity_fab)
    public void onFabClick(View v) {
        presenter.createNewGeneration();
    }

    @Override
    public void onShowNewGeneration() {
        startActivity(new Intent(this, GenerationActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
