package com.stedi.randomimagegenerator.app.other.logger;

import android.util.Log;

public class LogCatLogger implements Logger {
    private final String tag;

    public LogCatLogger(String tag) {
        this.tag = tag;
    }

    @Override
    public void log(Object from, String message) {
        Log.d(tag, from + ": " + message);
    }

    @Override
    public void log(Object from, Throwable t) {
        Log.d(tag, from + ": ", t);
    }

    @Override
    public void log(Object from, String message, Throwable t) {
        Log.d(tag, from + ": " + message, t);
    }
}
