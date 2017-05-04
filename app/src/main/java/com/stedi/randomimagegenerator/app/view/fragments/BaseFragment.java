package com.stedi.randomimagegenerator.app.view.fragments;

import android.support.v4.app.Fragment;

import com.stedi.randomimagegenerator.app.view.activity.BaseActivity;

public abstract class BaseFragment extends Fragment {
    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }
}
