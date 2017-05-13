package com.stedi.randomimagegenerator.app.view.dialogs;

import android.support.v4.app.DialogFragment;

import com.stedi.randomimagegenerator.app.view.activity.BaseActivity;

public abstract class BaseDialogFragment extends DialogFragment {
    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }
}
