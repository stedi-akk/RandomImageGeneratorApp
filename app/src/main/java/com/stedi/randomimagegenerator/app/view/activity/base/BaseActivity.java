package com.stedi.randomimagegenerator.app.view.activity.base;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.stedi.randomimagegenerator.app.di.components.ActivityComponent;
import com.stedi.randomimagegenerator.app.di.modules.ActivityModule;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.CommonKt;

import javax.inject.Inject;

public abstract class BaseActivity extends LifeCycleActivity {
    private ActivityComponent component;

    @Inject PendingPreset pendingPreset;
    @Inject CachedBus bus;

    private static boolean mustRestorePendingPreset = true;

    public static class PermissionEvent {
        public final String permission;
        public final int requestCode;
        public final boolean isGranted;

        PermissionEvent(String permission, int requestCode, boolean isGranted) {
            this.permission = permission;
            this.requestCode = requestCode;
            this.isGranted = isGranted;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getActivityComponent().inject(this);
        if (mustRestorePendingPreset && savedInstanceState != null) {
            pendingPreset.restore(savedInstanceState);
        }
        mustRestorePendingPreset = false;
        super.onCreate(savedInstanceState);
    }

    @NonNull
    public ActivityComponent getActivityComponent() {
        if (component == null)
            component = CommonKt.getApp(this).getComponent().plus(new ActivityModule(this));
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        pendingPreset.retain(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        bus.postDeadEvent(new PermissionEvent(permissions[0], requestCode, isGranted));
    }

    public boolean checkForPermission(@NonNull String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return false;
        }
        return true;
    }
}
