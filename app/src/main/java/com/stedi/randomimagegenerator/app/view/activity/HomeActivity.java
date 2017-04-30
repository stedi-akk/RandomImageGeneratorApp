package com.stedi.randomimagegenerator.app.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stedi.randomimagegenerator.app.di.modules.HomeModule;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.Utils;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.presenter.HomePresenter;

import java.util.List;

import javax.inject.Inject;

public class HomeActivity extends BaseActivity implements HomePresenter.UIImpl {
    @Inject HomePresenter presenter;
    @Inject Logger logger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().plus(new HomeModule()).inject(this);
        presenter.onAttach(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.fetchPresets();
    }

    @Override
    public void onPresetsFetched(@NonNull List<Preset> presets) {
        Utils.toast(this, "onPresetsFetched");
    }

    @Override
    public void onFailedToFetch(@NonNull Throwable t) {
        logger.log(this, t);
        Utils.toast(this, "onFailedToFetch");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
