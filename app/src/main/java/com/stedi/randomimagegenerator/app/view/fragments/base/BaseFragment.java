package com.stedi.randomimagegenerator.app.view.fragments.base;

import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity;

public abstract class BaseFragment extends LifeCycleFragment {
    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }
}
