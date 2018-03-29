package com.stedi.randomimagegenerator.app.other

import android.support.annotation.VisibleForTesting
import com.squareup.otto.Bus
import com.squareup.otto.DeadEvent
import com.squareup.otto.ThreadEnforcer
import com.stedi.randomimagegenerator.app.other.logger.Logger
import java.util.*

class CachedBus(
        enforcer: ThreadEnforcer = ThreadEnforcer.MAIN,
        private val logger: Logger) : Bus(enforcer) {

    private val cache = LinkedList<Runnable>()
    private val allowedDeadEvents = HashSet<Any>()
    private val creationThread = Thread.currentThread()

    private var locked: Boolean = false

    fun lock() {
        logger.log(this, "lock")
        ensureCreationThread()
        locked = true
    }

    fun unlock() {
        logger.log(this, "unlock")
        ensureCreationThread()
        locked = false
        releaseCache()
    }

    fun postDeadEvent(event: Any) {
        logger.log(this, "postDeadEvent")
        ensureCreationThread()
        allowedDeadEvents.add(event)
        post(event)
    }

    override fun post(event: Any) {
        logger.log(this, "posting $event")
        ensureCreationThread()
        if (!locked && event !is DeadEvent) {
            logger.log(this, "posting $event successfully")
            super.post(event)
            allowedDeadEvents.remove(event)
        } else {
            logger.log(this, "posting $event failed")
            var actualEvent = event
            if (event is DeadEvent) {
                actualEvent = event.event
                if (allowedDeadEvents.contains(actualEvent)) {
                    logger.log(this, "ignoring postDeadEvent event $actualEvent")
                    allowedDeadEvents.remove(actualEvent)
                    return
                }
            }
            logger.log(this, "adding to the cache")
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