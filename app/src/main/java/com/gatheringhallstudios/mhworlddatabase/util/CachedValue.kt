package com.gatheringhallstudios.mhworlddatabase.util

import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.schedule
import kotlin.concurrent.write

/**
 * Defines a single lazy evaluated variable with a timeout.
 * Retrieving a value runs the build() function, and begins the countdown to delete the cached value.
 * The timeout is extended every time get() is called.
 * The cached value is actively deleted to save space.
 */
class CachedValue<T>(
        private val timeout: Long,
        private val builder: () -> T
) {
    private val lock = ReentrantReadWriteLock()
    private val timer = Timer()

    private var task: TimerTask? = null
    private var hasValue = false
    private var cachedValue: T? = null

    /**
     * Retrieves the cached value if one exists, otherwise runs builder().
     * Resets the timeout once called.
     */
    @Suppress("UNCHECKED_CAST")
    fun get() : T {
        lock.read {
            if (hasValue) {
                val returnValue = cachedValue as T
                startTimeout()
                return returnValue
            }

            lock.write {
                hasValue = true
                cachedValue = builder()
                startTimeout()
                return cachedValue as T
            }
        }
    }

    private fun startTimeout() {
        task?.cancel()
        task = timer.schedule(timeout) {
            lock.write {
                hasValue = false
                cachedValue = null
            }
        }
    }
}