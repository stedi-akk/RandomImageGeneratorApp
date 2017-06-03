package com.stedi.randomimagegenerator.app.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.squareup.otto.Bus;
import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.di.components.ActivityComponent;
import com.stedi.randomimagegenerator.app.di.modules.ActivityModule;
import com.stedi.randomimagegenerator.app.other.CachedBus;

import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity {
    private ActivityComponent component;

    @Inject Bus bus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component = Components.getAppComponent(this).plus(new ActivityModule(this));
        component.inject(this);
    }

    @NonNull
    public ActivityComponent getActivityComponent() {
        return component;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bus instanceof CachedBus)
            ((CachedBus) bus).unlock();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bus instanceof CachedBus)
            ((CachedBus) bus).lock();
    }
}
