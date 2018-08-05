@file:JvmName("Functions")
package com.gatheringhallstudios.mhworlddatabase.util

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.res.Resources
import android.util.Log
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

const val TAG = "MHFunctions"

/**
 * Runs a function in a separate thread that logs the time taken, and any errors that occur.
 */
inline fun loggedThread(name: String? = null, crossinline process: () -> Unit) {
    val nameDisplay = name ?: "Unnamed"

    thread(start=true) {
        try {
            val timeToRun = measureTimeMillis(process)
            Log.d(TAG, "Ran $nameDisplay thread in $timeToRun milliseconds")
        } catch (ex: Exception) {
            Log.e(TAG, "Error in $nameDisplay thread", ex)
        }
    }
}

/**
 * Creates a livedata from a block of code that is run in another thread.
 * The other thread is run in a background thread, and not on the UI thread.
 */
fun <T> createLiveData(block: () -> T): LiveData<T> {
    val result = MutableLiveData<T>()
    loggedThread("createLiveData") {
        result.postValue(block())
    }
    return result
}

/**
 * Converts a measurement from DP to onscreen pixels, uses the system density
 */
fun dpToPx(dp: Int) =  (dp * Resources.getSystem().displayMetrics.density).toInt()