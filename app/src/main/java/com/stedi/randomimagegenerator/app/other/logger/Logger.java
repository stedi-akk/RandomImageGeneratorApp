package com.stedi.randomimagegenerator.app.other.logger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface Logger {
    void log(@NonNull Object from, @Nullable String message);

    void log(@NonNull Object from, @Nullable Throwable t);

    void log(@NonNull Object from, @Nullable String message, @Nullable Throwable t);
}
