package com.stedi.randomimagegenerator.app.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.other.logger.Logger;

import javax.inject.Inject;

public abstract class LifeCycleActivity extends AppCompatActivity {
    private static final boolean LOG = false;

    @Inject Logger logger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (LOG) {
            Components.getAppComponent(this).inject(this);
            logger.log(this, "onCreate");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        if (LOG)
            logger.log(this, "onRestart");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        if (LOG)
            logger.log(this, "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        if (LOG)
            logger.log(this, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (LOG)
            logger.log(this, "onPause");
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (LOG)
            logger.log(this, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        if (LOG)
            logger.log(this, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (LOG)
            logger.log(this, "onDestroy");
        super.onDestroy();
    }
}
