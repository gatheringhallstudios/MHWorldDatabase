package com.gatheringhallstudios.mhworlddatabase

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.util.Log
import com.gatheringhallstudios.mhworlddatabase.common.ColorRegistry
import com.gatheringhallstudios.mhworlddatabase.common.VectorRegistry
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable


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
    } catch (ex: Exception) {
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
fun Context.getAssetDrawable(path: String?, @DrawableRes default: Int): Drawable? {
    return this.getAssetDrawable(path) {
        ContextCompat.getDrawable(this, default)
    }
}

val PATH_NAME = "base"
/**
 * Extension: Loads a VectorDrawable and optional Color from the registry
 * and returns a colored Drawable.
 * Returns a ic_question_mark on failure.
 */
fun Context.getVectorDrawable(vector: String, color: String?): Drawable {
    // Get the drawable from the registry
    val drawable: VectorMasterDrawable
    if (VectorRegistry[vector] != null) {
        drawable = VectorMasterDrawable(this, VectorRegistry[vector]!!)
    } else {
        drawable = VectorMasterDrawable(this, R.drawable.question_mark_grey)
        return drawable
    }

    // Color the drawable from the registry
    if (ColorRegistry[color] != null) {
        val path = drawable.getPathModelByName(PATH_NAME)
        path.fillColor = ContextCompat.getColor(this, ColorRegistry[color]!!)
    }

    return drawable
}