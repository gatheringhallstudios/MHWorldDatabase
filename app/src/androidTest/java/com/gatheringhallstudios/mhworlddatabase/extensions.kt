package com.gatheringhallstudios.mhworlddatabase

import androidx.lifecycle.LiveData

/**
 * Extension: Synchronously waits for live data to produce a result, and then returns it.
 * Times out after 2 seconds with an InterruptedException
 */
inline fun <T> LiveData<T>.getResult() : T {
    return TestUtils.getValue(this)
}