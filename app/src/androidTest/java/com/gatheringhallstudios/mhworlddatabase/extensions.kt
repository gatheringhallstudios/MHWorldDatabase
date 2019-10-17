package com.gatheringhallstudios.mhworlddatabase

import androidx.lifecycle.LiveData
import com.squareup.leakcanary.AndroidDebuggerControl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

/**
 * Extension: Synchronously waits for live data to produce a result, and then returns it.
 * Times out after 2 seconds with an InterruptedException
 */
fun <T> LiveData<T>.getResult() : T {
    // contains the result, since lambdas can't assign to variables
    val value = AtomicReference<T>()

    val data = this
    val latch = CountDownLatch(1)

    GlobalScope.launch(Dispatchers.Main) {
        data.observeForever { result ->
            value.set(result)
            latch.countDown()
        }
    }

    latch.await(2, TimeUnit.SECONDS)
    return value.get()
}

