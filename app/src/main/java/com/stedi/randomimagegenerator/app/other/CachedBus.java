package com.stedi.randomimagegenerator.app.other;

import com.squareup.otto.Bus;

import java.util.LinkedList;

public class CachedBus extends Bus {
    private final LinkedList<Runnable> cache = new LinkedList<>();

    private final Thread creationThread = Thread.currentThread();

    private boolean locked;

    public void lock() {
        ensureCreationThread();
        locked = true;
    }

    public void unlock() {
        ensureCreationThread();
        locked = false;
        releaseCache();
    }

    @Override
    public void post(final Object event) {
        ensureCreationThread();
        if (!locked) {
            super.post(event);
        } else {
            cache.add(() -> post(event));
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
}
