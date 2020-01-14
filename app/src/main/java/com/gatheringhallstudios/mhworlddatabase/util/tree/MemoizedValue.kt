package com.gatheringhallstudios.mhworlddatabase.util.tree

import androidx.lifecycle.LiveData
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * Defines a single lazy evaluated variable using a key.
 * Retrieving a value runs the build() function unless cached. If using the same key value
 * as a previous run, the results of the previous run will be returned.
 * Otherwise it will create a new run.
 */
open class MemoizedValue<K, T> {
    private val lock = ReentrantReadWriteLock()

    private var hasValue = false
    private var lastKey: K? = null
    private var cachedValue: T? = null

    /**
     * Retrieves the cached value if one exists, otherwise runs builder().
     * Resets the timeout once called.
     */
    @Suppress("UNCHECKED_CAST")
    fun get(key: K, builder: () -> T) : T {
        lock.read {
            if (hasValue && lastKey == key) {
                return cachedValue as T
            }

            lock.write {
                hasValue = true
                cachedValue = builder()
                lastKey = key

                return cachedValue as T
            }
        }
    }
}

/**
 * A MemoizedValue that shortcuts to live data. Simplifies object declaration.
 */
class LiveMemoizedValue<K, T>: MemoizedValue<K, LiveData<T>>()