package com.stedi.randomimagegenerator.app.view.activity;

import android.support.v7.app.AppCompatActivity;

import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.di.components.ActivityComponent;
import com.stedi.randomimagegenerator.app.di.modules.ActivityModule;

public class BaseActivity extends AppCompatActivity {
    private ActivityComponent component;

    public ActivityComponent getComponent() {
        if (component == null)
            component = Components.getAppComponent(this).plus(new ActivityModule(this));
        return component;
    }
}
