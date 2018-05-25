package com.gatheringhallstudios.mhworlddatabase

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.Log
import android.util.TypedValue


val TAG = "MHWorldApplicationUtil"

/**
 * Extension: Loads a drawable from the assets folder. Returns null on failure.
 * Supply an optional default argument
 */
inline fun Context.getAssetDrawable(path: String?, default: () -> Drawable?): Drawable? {
    return try {
        this.assets.open(path).use {
            Drawable.createFromStream(it, path)
        }
    } catch(ex : Exception) {
        // Show a log error if we were expecting to get something.
        // If path is null or empty we weren't expecting anything
        if (path != null && path != "") {
            Log.e(TAG, "Failed to load asset file $path", ex)
        }

        default()
    }
}

inline fun Context.getAssetDrawable(path: String?): Drawable? {
    return this.getAssetDrawable(path) { null }
}

/**
 * Extension: Loads a drawable from the assets folder.
 * Returns a drawable resource on failure.
 */
fun Context.getAssetDrawable(path: String?, default: Int): Drawable? {
    return this.getAssetDrawable(path) {
        ContextCompat.getDrawable(this, default)
    }
}