package com.stedi.randomimagegenerator.app.view.fragments.base;

import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.App;
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity;

public abstract class BaseFragment extends LifeCycleFragment {
    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public boolean checkForPermission(@NonNull String permission, int requestCode) {
        return getBaseActivity().checkForPermission(permission, requestCode);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((App) getActivity().getApplicationContext()).getLeakWatcher().watch(this);
    }
}
