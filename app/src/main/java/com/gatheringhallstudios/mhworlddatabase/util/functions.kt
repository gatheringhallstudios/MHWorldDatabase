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
fun ConvertElevationToAlphaConvert(elevation: Int): Float {
    if (elevation >= 24) {
        return 0.16f
    } else if (elevation >= 16) {
        return 0.15f
    } else if (elevation >= 12) {
        return 0.14f
    } else if (elevation >= 8) {
        return 0.12f
    } else if (elevation >= 6) {
        return 0.11f
    } else if (elevation > 4) {
        return 0.09f
    } else if (elevation > 3) {
        return 0.08f
    } else if (elevation > 2) {
        return 0.07f
    } else if (elevation > 1) {
        return 0.05f
    } else {
        return 0f
    }
}
