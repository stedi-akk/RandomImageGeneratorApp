package com.stedi.randomimagegenerator.app.view.dialogs.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity;

public abstract class BaseDialogFragment extends DialogFragment {
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

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }
}
