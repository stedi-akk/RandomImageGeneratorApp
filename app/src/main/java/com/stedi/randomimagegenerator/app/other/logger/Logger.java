package com.stedi.randomimagegenerator.app.other.logger;

public interface Logger {
    void log(Object from, String message);

    void log(Object from, Throwable t);

    void log(Object from, String message, Throwable t);
}
