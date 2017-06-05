package com.stedi.randomimagegenerator.app.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.other.logger.Logger;

import javax.inject.Inject;

public abstract class LifeCycleActivity extends AppCompatActivity {
    private static final boolean LOG = true;

    @Inject Logger logger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LOG) {
            Components.getAppComponent(this).inject(this);
            logger.log(this, "onCreate");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (LOG)
            logger.log(this, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (LOG)
            logger.log(this, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LOG)
            logger.log(this, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (LOG)
            logger.log(this, "onPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (LOG)
            logger.log(this, "onSaveInstanceState");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (LOG)
            logger.log(this, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (LOG)
            logger.log(this, "onDestroy");
    }
}
