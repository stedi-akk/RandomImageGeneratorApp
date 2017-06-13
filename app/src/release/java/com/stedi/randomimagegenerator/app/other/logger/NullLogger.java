package com.stedi.randomimagegenerator.app.other.logger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class NullLogger implements Logger {
    @Override
    public void log(@NonNull Object from, @Nullable String message) {

    }

    @Override
    public void log(@NonNull Object from, @Nullable Throwable t) {

    }

    @Override
    public void log(@NonNull Object from, @Nullable String message, @Nullable Throwable t) {

    }
}
