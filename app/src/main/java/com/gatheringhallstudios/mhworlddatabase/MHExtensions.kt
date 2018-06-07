package com.gatheringhallstudios.mhworlddatabase

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.util.Log
import com.gatheringhallstudios.mhworlddatabase.common.ColorRegistry
import com.gatheringhallstudios.mhworlddatabase.common.VectorRegistry
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable
import com.sdsmdg.harjot.vectormaster.models.PathModel


val TAG = "MHWorldApplicationUtil"


/**
 * Extension: Loads a drawable from the assets folder.
 * Returns null on failure, or default resource if provided.
 */
fun Context.getAssetDrawable(
        path: String?,
        @DrawableRes default: Int = R.drawable.question_mark_grey): Drawable? {
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

        return ContextCompat.getDrawable(this, default)
    }
}

/**
 * Extension: Loads a VectorDrawable and optional Color from the registry
 * and returns the colored vector.
 */
fun Context.getVectorDrawable(
        @DrawableRes vector: Int,
        color: String? = null
): Drawable {

    val drawable = VectorMasterDrawable(this, vector)

    if (color == null) {
        return drawable
    }

    val registryValue = ColorRegistry[color]
    if (registryValue == null) {
        Log.w(TAG, "Color registry value does not exist: $color")
        return drawable
    }

    // TODO Update to support coloring multiple paths if necessary. i.e. base1, base2, base3
    val path: PathModel? = drawable.getPathModelByName(PATH_NAME)
    if (path == null) {
        Log.w(TAG, "Could not find path $PATH_NAME in vector")
    } else {
        path.fillColor = ContextCompat.getColor(this, registryValue)
    }

    return drawable
}

val PATH_NAME = "base"
/**
 * Extension: Loads a VectorDrawable and optional Color from the registry
 * and returns a colored Drawable.
 * Returns the default resource on failure.
 * @param A vector registry value
 */
fun Context.getVectorDrawable(
        vector: String,
        color: String = "rare1",
        @DrawableRes default: Int = R.drawable.question_mark_grey
): Drawable? {

    // Get the drawable from the registry. or return default
    val resource = VectorRegistry[vector]

    return if (resource != null) {
        this.getVectorDrawable(resource, color)
    } else {
        Log.e(TAG, "Failed to load vector in the registry: $vector")
        ContextCompat.getDrawable(this, default)
    }
}