package com.stedi.randomimagegenerator.app.other;

import android.support.test.runner.AndroidJUnit4;

import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;
import com.stedi.randomimagegenerator.app.other.logger.SoutLogger;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CachedBusTest {
    private static class TestEvent {
    }

    private static class TestTarget {
        private int eventsCount;

        @Subscribe
        public void onEvent(TestEvent testEvent) {
            eventsCount++;
        }
    }

    @Test
    public void unlockedTest() {
        CachedBus bus = new CachedBus(ThreadEnforcer.ANY, new SoutLogger("CachedBusTest"));
        TestTarget testTarget = new TestTarget();

        bus.unlock();
        bus.register(testTarget);
        assertTrue(bus.getCache().isEmpty());

        bus.post(new TestEvent());
        assertTrue(testTarget.eventsCount == 1);
        assertTrue(bus.getCache().isEmpty());
        bus.post(new TestEvent());
        assertTrue(testTarget.eventsCount == 2);
        assertTrue(bus.getCache().isEmpty());
        bus.post(new TestEvent());
        bus.post(new TestEvent());
        assertTrue(testTarget.eventsCount == 4);
        assertTrue(bus.getCache().isEmpty());
    }

    @Test
    public void lockedTest() {
        CachedBus bus = new CachedBus(ThreadEnforcer.ANY, new SoutLogger("CachedBusTest"));
        TestTarget testTarget = new TestTarget();

        bus.lock();
        bus.register(testTarget);
        assertTrue(bus.getCache().isEmpty());

        bus.post(new TestEvent());
        assertTrue(testTarget.eventsCount == 0);
        assertTrue(bus.getCache().size() == 1);
        bus.post(new TestEvent());
        assertTrue(testTarget.eventsCount == 0);
        assertTrue(bus.getCache().size() == 2);
        bus.post(new TestEvent());
        bus.post(new TestEvent());
        assertTrue(testTarget.eventsCount == 0);
        assertTrue(bus.getCache().size() == 4);
    }

    @Test
    public void lockedUnlockedTest() {
        CachedBus bus = new CachedBus(ThreadEnforcer.ANY, new SoutLogger("CachedBusTest"));
        TestTarget testTarget = new TestTarget();

        bus.lock();
        bus.register(testTarget);
        assertTrue(bus.getCache().isEmpty());

        bus.post(new TestEvent());
        assertTrue(testTarget.eventsCount == 0);
        assertTrue(bus.getCache().size() == 1);
        bus.unlock();
        assertTrue(testTarget.eventsCount == 1);
        assertTrue(bus.getCache().isEmpty());

        bus.post(new TestEvent());
        assertTrue(testTarget.eventsCount == 2);
        assertTrue(bus.getCache().isEmpty());
        bus.lock();

        bus.post(new TestEvent());
        assertTrue(testTarget.eventsCount == 2);
        assertTrue(bus.getCache().size() == 1);
        bus.post(new TestEvent());
        bus.post(new TestEvent());
        assertTrue(testTarget.eventsCount == 2);
        assertTrue(bus.getCache().size() == 3);

        bus.unlock();
        assertTrue(testTarget.eventsCount == 5);
        assertTrue(bus.getCache().isEmpty());
    }

    @Test
    public void postDeadTest() {
        CachedBus bus = new CachedBus(ThreadEnforcer.ANY, new SoutLogger("CachedBusTest"));
        TestTarget testTarget = new TestTarget();

        bus.register(testTarget);
        bus.unlock();

        bus.postDeadEvent(new TestEvent());
        assertTrue(testTarget.eventsCount == 1);
        assertTrue(bus.getAllowedDeadEvents().isEmpty());

        bus.lock();
        bus.postDeadEvent(new TestEvent());
        assertTrue(testTarget.eventsCount == 1);
        assertTrue(bus.getAllowedDeadEvents().size() == 1);
        assertTrue(bus.getCache().size() == 1);

        bus.unregister(testTarget);
        bus.unlock();

        assertTrue(testTarget.eventsCount == 1);
        assertTrue(bus.getAllowedDeadEvents().isEmpty());
        assertTrue(bus.getCache().isEmpty());

        bus.register(testTarget);
        bus.postDeadEvent(new TestEvent());
        assertTrue(testTarget.eventsCount == 2);
        assertTrue(bus.getAllowedDeadEvents().isEmpty());
        assertTrue(bus.getCache().isEmpty());
    }
}