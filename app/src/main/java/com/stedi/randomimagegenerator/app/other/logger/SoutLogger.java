package com.stedi.randomimagegenerator.app.other.logger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class SoutLogger implements Logger {
    private final String tag;

    public SoutLogger(@NonNull String tag) {
        this.tag = tag;
    }

    @Override
    public void log(@NonNull Object from, @Nullable String message) {
        System.out.println(tag + ": " + from.getClass().getSimpleName() + ": " + message);
    }

    @Override
    public void log(@NonNull Object from, @Nullable Throwable t) {
        System.out.println(tag + ": " + from.getClass().getSimpleName() + ": Throwable: ");
        if (t != null)
            t.printStackTrace();
    }

    @Override
    public void log(@NonNull Object from, @Nullable String message, @Nullable Throwable t) {
        System.out.println(tag + ": " + from.getClass().getSimpleName() + ": " + message + "; Throwable: ");
        if (t != null)
            t.printStackTrace();
    }
}
