package com.stedi.randomimagegenerator.app.other;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.squareup.otto.Bus;
import com.squareup.otto.DeadEvent;
import com.squareup.otto.ThreadEnforcer;
import com.stedi.randomimagegenerator.app.other.logger.Logger;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class CachedBus extends Bus {
    private final LinkedList<Runnable> cache = new LinkedList<>();
    private final Set<Object> postDeadEvents = new HashSet<>();
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

    public void postDead(final Object event) {
        logger.log(this, "postDead");
        ensureCreationThread();
        postDeadEvents.add(event);
        post(event);
    }

    @Override
    public void post(final Object event) {
        logger.log(this, "posting " + event);
        ensureCreationThread();
        if (!locked && !(event instanceof DeadEvent)) {
            logger.log(this, "posting " + event + " successfully");
            super.post(event);
            postDeadEvents.remove(event);
        } else {
            logger.log(this, "posting " + event + " failed");
            Object actualEvent = event;
            if (event instanceof DeadEvent) {
                actualEvent = ((DeadEvent) event).event;
                if (postDeadEvents.contains(actualEvent)) {
                    logger.log(this, "ignoring postDead event " + actualEvent);
                    postDeadEvents.remove(actualEvent);
                    return;
                }
            }
            logger.log(this, "adding to the cache");
            Object actualEventF = actualEvent;
            cache.add(() -> post(actualEventF));
        }
    }

    private void releaseCache() {
        if (!cache.isEmpty()) {
            LinkedList<Runnable> release = new LinkedList<>(cache);
            cache.clear();
            while (!release.isEmpty())
                release.pollFirst().run();
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
