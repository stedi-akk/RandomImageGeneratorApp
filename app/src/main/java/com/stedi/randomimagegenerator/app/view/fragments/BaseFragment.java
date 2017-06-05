package com.stedi.randomimagegenerator.app.view.fragments;

import com.stedi.randomimagegenerator.app.view.activity.BaseActivity;

public abstract class BaseFragment extends LifeCycleFragment {
    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }
}
