package com.stedi.randomimagegenerator.app.view.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.di.components.ActivityComponent;
import com.stedi.randomimagegenerator.app.di.modules.ActivityModule;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.Utils;

import javax.inject.Inject;

public abstract class BaseActivity extends LifeCycleActivity {
    private ActivityComponent component;

    @Inject CachedBus bus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getActivityComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @NonNull
    public ActivityComponent getActivityComponent() {
        if (component == null)
            component = Components.getAppComponent(this).plus(new ActivityModule(this));
        return component;
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.unlock();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bus.lock();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED)
            Utils.toastShort(this, "permission " + permissions[0] + " is required");
    }

    public boolean checkForPermission(@NonNull String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return false;
        }
        return true;
    }
}
