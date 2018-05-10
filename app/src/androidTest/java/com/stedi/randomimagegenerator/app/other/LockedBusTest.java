package com.stedi.randomimagegenerator.app.other;

import android.support.test.runner.AndroidJUnit4;

import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class LockedBusTest {
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
        LockedBus bus = new LockedBus(ThreadEnforcer.ANY);
        TestTarget testTarget = new TestTarget();

        bus.unlock();
        bus.register(testTarget);
        assertTrue(bus.getLockedEvents().isEmpty());

        bus.post(new TestEvent());
        assertTrue(testTarget.eventsCount == 1);
        assertTrue(bus.getLockedEvents().isEmpty());
        bus.post(new TestEvent());
        assertTrue(testTarget.eventsCount == 2);
        assertTrue(bus.getLockedEvents().isEmpty());
        bus.post(new TestEvent());
        bus.post(new TestEvent());
        assertTrue(testTarget.eventsCount == 4);
        assertTrue(bus.getLockedEvents().isEmpty());
    }

    @Test
    public void lockedTest() {
        LockedBus bus = new LockedBus(ThreadEnforcer.ANY);
        TestTarget testTarget = new TestTarget();

        bus.lock();
        bus.register(testTarget);
        assertTrue(bus.getLockedEvents().isEmpty());

        bus.post(new TestEvent());
        assertTrue(testTarget.eventsCount == 0);
        assertTrue(bus.getLockedEvents().size() == 1);
        bus.post(new TestEvent());
        assertTrue(testTarget.eventsCount == 0);
        assertTrue(bus.getLockedEvents().size() == 2);
        bus.post(new TestEvent());
        bus.post(new TestEvent());
        assertTrue(testTarget.eventsCount == 0);
        assertTrue(bus.getLockedEvents().size() == 4);
    }

    @Test
    public void lockedUnlockedTest() {
        LockedBus bus = new LockedBus(ThreadEnforcer.ANY);
        TestTarget testTarget = new TestTarget();

        bus.lock();
        bus.register(testTarget);
        assertTrue(bus.getLockedEvents().isEmpty());

        bus.post(new TestEvent());
        assertTrue(testTarget.eventsCount == 0);
        assertTrue(bus.getLockedEvents().size() == 1);
        bus.unlock();
        assertTrue(testTarget.eventsCount == 1);
        assertTrue(bus.getLockedEvents().isEmpty());

        bus.post(new TestEvent());
        assertTrue(testTarget.eventsCount == 2);
        assertTrue(bus.getLockedEvents().isEmpty());
        bus.lock();

        bus.post(new TestEvent());
        assertTrue(testTarget.eventsCount == 2);
        assertTrue(bus.getLockedEvents().size() == 1);
        bus.post(new TestEvent());
        bus.post(new TestEvent());
        assertTrue(testTarget.eventsCount == 2);
        assertTrue(bus.getLockedEvents().size() == 3);

        bus.unlock();
        assertTrue(testTarget.eventsCount == 5);
        assertTrue(bus.getLockedEvents().isEmpty());
    }
}
