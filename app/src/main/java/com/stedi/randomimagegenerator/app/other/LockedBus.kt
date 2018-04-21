package com.stedi.randomimagegenerator.app.other

import com.squareup.otto.Bus
import com.squareup.otto.ThreadEnforcer
import timber.log.Timber
import java.util.*
import javax.inject.Singleton

@Singleton
class LockedBus(enforcer: ThreadEnforcer = ThreadEnforcer.MAIN) : Bus(enforcer) {

    private val lockedEvents = LinkedList<Runnable>()
    private val creationThread = Thread.currentThread()

    private var locked: Boolean = false

    fun lock() {
        Timber.d("locked")
        ensureCreationThread()
        locked = true
    }

    fun unlock() {
        Timber.d("unlocked")
        ensureCreationThread()
        locked = false
        releaseLockedEvents()
    }

    override fun post(event: Any) {
        ensureCreationThread()
        if (!locked) {
            Timber.d("posting $event successfully")
            super.post(event)
        } else {
            Timber.d("posting $event failed. adding to the cache")
            lockedEvents.add(Runnable { post(event) })
        }
    }

    private fun releaseLockedEvents() {
        if (!lockedEvents.isEmpty()) {
            val release = LinkedList(lockedEvents)
            lockedEvents.clear()
            while (!release.isEmpty()) {
                release.pollFirst().run()
            }
        }
    }

    private fun ensureCreationThread() {
        if (Thread.currentThread() !== creationThread) {
            throw IllegalStateException("LockedBus requires the thread from which it was created")
        }
    }
}