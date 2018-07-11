package com.gatheringhallstudios.mhworlddatabase

import android.arch.lifecycle.LiveData
import android.support.v4.app.Fragment
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

val TAG = "MHWorldApplicationUtil"

/**
 * Returns the data contained in the livedata, waiting up to 2 seconds to retrieve it
 */
fun <T> LiveData<T>.getResult(): T {
    // contains the result, since lambdas can't assign to variables
    val value = AtomicReference<T>()

    val latch = CountDownLatch(1)
    this.observeForever { result ->
        value.set(result)
        latch.countDown()
    }
    latch.await(2, TimeUnit.SECONDS)
    return value.get()
}

/**
 * Returns the router object that can be used to navigate between pages
 */
fun Fragment.getRouter() : Router {
    return Router(this.findNavController())
}

/**
 * Returns the router object that can be used to navigate between pages
 */
fun View.getRouter() : Router {
    return Router(this.findNavController())
}