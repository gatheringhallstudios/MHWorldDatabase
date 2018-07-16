package com.gatheringhallstudios.mhworlddatabase

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.os.Bundle
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

fun <T: Fragment> T.applyArguments(block: Bundle.() -> Unit): T {
    val bundle = Bundle()
    bundle.block()
    this.arguments = bundle
    return this
}

/**
 * Merges multiple live data into a single result
 */
fun <T> mergeLiveDataMany(vararg data: LiveData<*>, transform: (List<*>) -> T): LiveData<T> {
    return MediatorLiveData<T>().apply {
        var encountered = 0
        val results = MutableList<Any?>(data.size) { null }

        for ((idx, subData) in data.withIndex()) {
            addSource(subData) {
                results[idx] = it
                encountered++

                if (encountered == data.size) {
                    this.value = transform(results)
                }
            }
        }
    }
}

fun <T, A, B> mergeLiveData(a: LiveData<A>, b: LiveData<B>, transform: (Pair<A, B>) -> T): LiveData<T> {
    return mergeLiveDataMany(a, b) {
        @Suppress("UNCHECKED_CAST")
        val result = Pair(it[0] as A, it[1] as B)
        transform(result)
    }
}

fun <T, A, B, C> mergeLiveData(
        a: LiveData<A>,
        b: LiveData<B>,
        c: LiveData<C>,
        transform: (Triple<A, B, C>) -> T): LiveData<T> {
    return mergeLiveDataMany(a, b, c) {
        @Suppress("UNCHECKED_CAST")
        val result = Triple(it[0] as A, it[1] as B, it[2] as C)
        transform(result)
    }
}