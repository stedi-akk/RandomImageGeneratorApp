package com.stedi.randomimagegenerator.app.other.logger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class LogCatLogger implements Logger {
    private final String tag;

    public LogCatLogger(@NonNull String tag) {
        this.tag = tag;
    }

    @Override
    public void log(@NonNull Object from, @Nullable String message) {
        Log.d(tag, from.getClass().getSimpleName() + ": " + message);
    }

    @Override
    public void log(@NonNull Object from, @Nullable Throwable t) {
        Log.d(tag, from.getClass().getSimpleName() + ": ", t);
    }

    @Override
    public void log(@NonNull Object from, @Nullable String message, @Nullable Throwable t) {
        Log.d(tag, from.getClass().getSimpleName() + ": " + message, t);
    }
}
