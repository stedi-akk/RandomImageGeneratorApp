package com.stedi.randomimagegenerator.app.view.dialogs.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;

import com.stedi.randomimagegenerator.app.App;
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity;

public abstract class BaseDialogFragment extends AppCompatDialogFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((App) getActivity().getApplicationContext()).getLeakWatcher().watch(this);
    }

    public void show(FragmentManager manager) {
        show(manager, getClass().getSimpleName());
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }
}
