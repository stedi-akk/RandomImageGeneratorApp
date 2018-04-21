package com.stedi.randomimagegenerator.app.other

import android.support.annotation.VisibleForTesting
import com.squareup.otto.Bus
import com.squareup.otto.DeadEvent
import com.squareup.otto.ThreadEnforcer
import timber.log.Timber
import java.util.*
import javax.inject.Singleton

@Singleton
class CachedBus(enforcer: ThreadEnforcer = ThreadEnforcer.MAIN) : Bus(enforcer) {

    private val cache = LinkedList<Runnable>()
    private val allowedDeadEvents = HashSet<Any>()
    private val creationThread = Thread.currentThread()

    private var locked: Boolean = false

    fun lock() {
        Timber.d("lock")
        ensureCreationThread()
        locked = true
    }

    fun unlock() {
        Timber.d("unlock")
        ensureCreationThread()
        locked = false
        releaseCache()
    }

    fun postDeadEvent(event: Any) {
        Timber.d("postDeadEvent")
        ensureCreationThread()
        allowedDeadEvents.add(event)
        post(event)
    }

    override fun post(event: Any) {
        Timber.d("posting $event")
        ensureCreationThread()
        if (!locked && event !is DeadEvent) {
            Timber.d("posting $event successfully")
            super.post(event)
            allowedDeadEvents.remove(event)
        } else {
            Timber.d("posting $event failed")
            var actualEvent = event
            if (event is DeadEvent) {
                actualEvent = event.event
                if (allowedDeadEvents.contains(actualEvent)) {
                    Timber.d("ignoring postDeadEvent event $actualEvent")
                    allowedDeadEvents.remove(actualEvent)
                    return
                }
            }
            Timber.d("adding to the cache")
            cache.add(Runnable { post(actualEvent) })
        }
    }

    private fun releaseCache() {
        if (!cache.isEmpty()) {
            val release = LinkedList(cache)
            cache.clear()
            while (!release.isEmpty()) {
                release.pollFirst().run()
            }
        }
    }

    private fun ensureCreationThread() {
        if (Thread.currentThread() !== creationThread) {
            throw IllegalStateException("CachedBus requires the thread from which it was created")
        }
    }

    @VisibleForTesting
    fun getCache() = cache

    @VisibleForTesting
    fun getAllowedDeadEvents() = allowedDeadEvents
}