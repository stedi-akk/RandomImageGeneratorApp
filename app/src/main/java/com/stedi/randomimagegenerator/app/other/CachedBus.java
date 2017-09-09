package com.stedi.randomimagegenerator.app.other;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.squareup.otto.Bus;
import com.squareup.otto.DeadEvent;
import com.squareup.otto.ThreadEnforcer;
import com.stedi.randomimagegenerator.app.other.logger.Logger;

import java.util.LinkedList;

public class CachedBus extends Bus {
    private final LinkedList<Runnable> cache = new LinkedList<>();
    private final Thread creationThread = Thread.currentThread();

    private final Logger logger;

    private boolean locked;

    public CachedBus(@NonNull Logger logger) {
        this(ThreadEnforcer.MAIN, logger);
    }

    public CachedBus(@NonNull ThreadEnforcer enforcer, @NonNull Logger logger) {
        super(enforcer);
        this.logger = logger;
    }

    public void lock() {
        logger.log(this, "lock");
        ensureCreationThread();
        locked = true;
    }

    public void unlock() {
        logger.log(this, "unlock");
        ensureCreationThread();
        locked = false;
        releaseCache();
    }

    @Override
    public void post(final Object event) {
        logger.log(this, "posting " + event);
        ensureCreationThread();
        if (!locked && !(event instanceof DeadEvent)) {
            logger.log(this, "posting " + event + " successfully");
            super.post(event);
        } else {
            logger.log(this, "posting " + event + " failed because of lock or it is a DeadEvent, adding to the cache");
            cache.add(() -> post(event instanceof DeadEvent ? ((DeadEvent) event).event : event));
        }
    }

    private void releaseCache() {
        if (!cache.isEmpty()) {
            LinkedList<Runnable> release = new LinkedList<>(cache);
            cache.clear();
            while (!release.isEmpty())
                release.pollLast().run();
        }
    }

    private void ensureCreationThread() {
        if (Thread.currentThread() != creationThread)
            throw new IllegalStateException("CachedBus requires the thread from which it was created");
    }

    @VisibleForTesting
    LinkedList<Runnable> getCache() {
        return cache;
    }
}
