package com.stedi.randomimagegenerator.app;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

import com.github.tmurakami.dexopener.DexOpener;

public class MyAndroidJUnitRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        DexOpener.builder(context)
                .buildConfig(BuildConfig.class)
                .build()
                .installTo(cl);
        return super.newApplication(cl, className, context);
    }
}
