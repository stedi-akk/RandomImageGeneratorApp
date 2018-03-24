package com.stedi.randomimagegenerator.app.di;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.stedi.randomimagegenerator.app.App;
import com.stedi.randomimagegenerator.app.di.components.ActivityComponent;
import com.stedi.randomimagegenerator.app.di.components.AppComponent;
import com.stedi.randomimagegenerator.app.di.components.GenerationComponent;
import com.stedi.randomimagegenerator.app.view.activity.GenerationStepsActivity;
import com.stedi.randomimagegenerator.app.view.activity.base.BaseActivity;

public final class Components {
    private Components() {
    }

    public static AppComponent getAppComponent(@NonNull Context context) {
        return ((App) context.getApplicationContext()).getComponent();
    }

    public static ActivityComponent getActivityComponent(@NonNull Activity activity) {
        return ((BaseActivity) activity).getActivityComponent();
    }

    public static ActivityComponent getActivityComponent(@NonNull Fragment fragment) {
        return getActivityComponent(fragment.getActivity());
    }

    public static GenerationComponent getGenerationComponent(@NonNull Fragment fragment) {
        return ((GenerationStepsActivity) fragment.getActivity()).getGenerationComponent();
    }
}
