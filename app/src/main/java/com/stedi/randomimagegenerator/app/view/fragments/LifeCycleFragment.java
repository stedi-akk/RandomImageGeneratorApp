package com.stedi.randomimagegenerator.app.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.other.logger.Logger;

import javax.inject.Inject;

public abstract class LifeCycleFragment extends Fragment {
    private static final boolean LOG = true;

    @Inject Logger logger;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (LOG) {
            Components.getAppComponent(context).inject(this);
            logger.log(this, "onAttach");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LOG)
            logger.log(this, "onCreate");
    }

    @CallSuper
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (LOG)
            logger.log(this, "onCreateView");
        return null;
    }

    @CallSuper
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (LOG)
            logger.log(this, "onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (LOG)
            logger.log(this, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (LOG)
            logger.log(this, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LOG)
            logger.log(this, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (LOG)
            logger.log(this, "onPause");
    }

    @CallSuper
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (LOG)
            logger.log(this, "onSaveInstanceState");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (LOG)
            logger.log(this, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (LOG)
            logger.log(this, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (LOG)
            logger.log(this, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (LOG)
            logger.log(this, "onDetach");
    }
}
