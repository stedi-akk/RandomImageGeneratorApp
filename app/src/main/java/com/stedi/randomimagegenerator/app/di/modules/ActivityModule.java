package com.stedi.randomimagegenerator.app.di.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.view.activity.BaseActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private final BaseActivity activity;

    public ActivityModule(@NonNull BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    @Named("ActivityContext")
    Context provideActivityContext() {
        return activity;
    }
}
