package com.stedi.randomimagegenerator.app.other.logger;

public class NullLogger implements Logger {
    @Override
    public void log(Object from, String message) {

    }

    @Override
    public void log(Object from, Throwable t) {

    }

    @Override
    public void log(Object from, String message, Throwable t) {

    }
}
