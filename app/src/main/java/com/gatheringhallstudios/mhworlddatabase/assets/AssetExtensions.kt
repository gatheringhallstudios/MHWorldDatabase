package com.gatheringhallstudios.mhworlddatabase.assets

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable
import com.sdsmdg.harjot.vectormaster.models.PathModel
import java.io.FileNotFoundException


val TAG = "MHWorldAssetUtil"
val PATH_NAME = "base"


/**
 * Extension: Receives an asset loader for a fragment
 */
val Fragment.assetLoader get() = AssetLoader(context!!)

/**
 * Extension: Receives an asset loader for a view
 */
val View.assetLoader get() = AssetLoader(context!!)

/**
 * Extension: Loads a drawable from the assets folder.
 * Returns null on failure, or default resource if provided.
 */
fun Context.getAssetDrawable(
        path: String?,
        @DrawableRes default: Int = R.drawable.ic_question_mark): Drawable? {
    return try {
        this.assets.open(path).use {
            Drawable.createFromStream(it, path)
        }
    } catch (ex: Exception) {
        // Show a log error if we were expecting to get something.
        // If path is null or empty we weren't expecting anything
        if (path != null && path != "") {
            if (ex is FileNotFoundException) {
                Log.e(TAG, "Failed to load asset file $path: Does not exist")
            } else {
                Log.e(TAG, "Failed to load asset file $path", ex)
            }
        }

        return ContextCompat.getDrawable(this, default)
    }
}

// Contains all instances of vector/color combinations
// The actual draws of this go into an LruCache, but if this causes a leak:
//  1) Create wrapper that change the color just before a draw (so we only store base vectors)
private val vectorCache = mutableMapOf<Pair<String, String>, Drawable>()

/**
 * Extension: Loads a VectorDrawable and optional Color from the normal registry
 * and returns a colored Drawable.
 * Returns the default resource on failure.
 * @param A vector registry value
 */
fun Context.getVectorDrawable(
        vector: String,
        color: String?,
        @DrawableRes default: Int = R.drawable.ic_question_mark
): Drawable? {
    val key = Pair(vector, color ?: "")

    // Get the drawable from the registry. or return default
    val resource = VectorRegistry(vector)
    if (resource == null) {
        Log.e(TAG, "Failed to load vector in the registry: $vector")
        return this.getDrawableCompat(default)
    }

    return vectorCache.getOrPut(key) {
        val drawable = VectorMasterDrawable(this, resource)

        if (color == null) {
            return@getOrPut CachedDrawable(drawable)
        }

        val registryValue = ColorRegistry(color)
        if (registryValue == null) {
            Log.w(TAG, "Color registry value does not exist: $color")
            return@getOrPut CachedDrawable(drawable)
        }

        // TODO Update to support coloring multiple paths if necessary. i.e. base1, base2, base3
        val path: PathModel? = drawable.getPathModelByName(PATH_NAME)
        if (path == null) {
            Log.w(TAG, "Could not find path $PATH_NAME in vector")
        } else {
            path.fillColor = ContextCompat.getColor(this, registryValue)
        }

        CachedDrawable(drawable)
    }
}