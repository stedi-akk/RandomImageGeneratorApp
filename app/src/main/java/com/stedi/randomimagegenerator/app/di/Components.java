package com.stedi.randomimagegenerator.app.di;

import android.app.Activity;
import android.content.Context;

import com.stedi.randomimagegenerator.app.App;
import com.stedi.randomimagegenerator.app.di.components.ActivityComponent;
import com.stedi.randomimagegenerator.app.di.components.AppComponent;
import com.stedi.randomimagegenerator.app.view.activity.BaseActivity;

public final class Components {
    private Components() {
    }

    public static AppComponent getAppComponent(Context context) {
        return ((App) context.getApplicationContext()).getComponent();
    }

    public static ActivityComponent getActivityComponent(Activity activity) {
        return ((BaseActivity) activity).getComponent();
    }
}
