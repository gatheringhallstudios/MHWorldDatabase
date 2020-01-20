@file:JvmName("Functions")

package com.gatheringhallstudios.mhworlddatabase.util

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

const val TAG = "MHFunctions"

/**
 * Runs a function in a separate thread that logs the time taken, and any errors that occur.
 */
inline fun loggedThread(name: String? = null, crossinline process: () -> Unit) {
    val nameDisplay = name ?: "Unnamed"

    thread(start = true) {
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
fun dpToPx(dp: Int) = (dp * Resources.getSystem().displayMetrics.density).toInt()

/**
 * Converts an elevation to an alpha value for a material design card overlay
 */
fun elevationToAlpha(elevation: Int): Float {
    return when {
        elevation >= 24 -> 0.48f
        elevation >= 16 -> 0.45f
        elevation >= 12 -> 0.42f
        elevation >= 8 -> 0.36f
        elevation >= 6 -> 0.33f
        elevation > 4 -> 0.27f
        elevation > 3 -> 0.24f
        elevation > 2 -> 0.21f
        elevation > 1 -> 0.15f
        else -> 0f
    }
}
