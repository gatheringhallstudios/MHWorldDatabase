package com.gatheringhallstudios.mhworlddatabase.util

import androidx.lifecycle.*
import kotlin.reflect.KProperty

/**
 * Creates a new DataWatcher class. Used via Kotlin delegation.
 *
 * Example:
 * class SomeHolder : DataSynchronizer() {
 *     var data: String by DataWatcher(this)
 *     var otherData: Int by DataWatcher(this)
 * }
 */
@Suppress("UNCHECKED_CAST")
class DataWatcher<T>(private val parent: DataSynchronizer) {
    var initialized = false
        private set

    private var value: T? = null

    init {
        parent.addWatcher(this)
    }

    operator fun getValue(thisRef: DataSynchronizer, property: KProperty<*>): T {
        if (!initialized) {
            throw UninitializedPropertyAccessException("Cannot call getValue() without first assigning a value (${property.name})")
        }

        return value as T
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value

        // organized like this for "some semblance" of thread safety
        val wasInitialized = initialized
        initialized = true

        if (!wasInitialized) {
            parent.notifyWatcherInitialized()
        }
    }
}

/**
 * An abstract superclass for data consolidation.
 * Register data watchers by assignment them via kotlin delegates using the "by" keyword"
 * Temporary solution until coroutines are stable in Kotlin, which are a superior solution.
 */
abstract class DataSynchronizer {
    private val watchers = mutableListOf<DataWatcher<*>>()
    private val initializedEvent = MutableLiveData<Boolean>()

    /**
     * Set to true when all waiting data has been set
     */
    var allInitialized: Boolean = false
        private set

    /**
     * An event that triggers once all data is loaded. Accepts a lifecycle owner
     * and activates in the UI thread.
     */
    fun observeAllLoaded(owner: LifecycleOwner, callback: () -> Unit) {
        initializedEvent.observe(owner, Observer {
            callback()
        })
    }

    /**
     * Adds a watcher to be managed. Added automatically when a watcher is initialized.
     */
    internal fun addWatcher(watcher: DataWatcher<*>) {
        watchers.add(watcher)
    }

    /**
     * Internal function called by DataWatcher to notify that it has initialized.
     * Will trigger the all load event if all watchers are loaded.
     */
    internal fun notifyWatcherInitialized() {
        val allInitialized = watchers.all { it.initialized }
        this.allInitialized = allInitialized

        if (allInitialized) {
            initializedEvent.postValue(true)
        }
    }
}