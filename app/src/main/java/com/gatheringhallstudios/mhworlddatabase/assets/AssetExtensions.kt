package com.gatheringhallstudios.mhworlddatabase.assets

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable
import com.sdsmdg.harjot.vectormaster.models.PathModel
import java.io.FileNotFoundException


val TAG = "MHWorldAssetUtil"

/**
 * Extension: Loads a drawable from the assets folder.
 * Returns null on failure, or default resource if provided.
 */
fun Context.getAssetDrawable(
        path: String,
        @DrawableRes default: Int = R.drawable.ic_question_mark): Drawable? {
    return try {
        this.assets.open(path).use {
            Drawable.createFromStream(it, path)
        }
    } catch (ex: Exception) {
        // Show a log error if we were expecting to get something.
        // If path is null or empty we weren't expecting anything
        if (path != "") {
            if (ex is FileNotFoundException) {
                Log.e(TAG, "Failed to load asset file $path: Does not exist")
            } else {
                Log.e(TAG, "Failed to load asset file $path", ex)
            }
        }

        return AppCompatResources.getDrawable(this, default)
    }
}

// Contains all base vectors
// The actual draws of this go into an LruCache
private val vectorCache = mutableMapOf<String, VectorMasterDrawable>()

/**
 * Extension: Loads a VectorDrawable and optional Color from the normal registry
 * and returns a colored Drawable.
 * Returns the default resource on failure.
 * @param A vector registry value
 */
fun Context.getVectorDrawable(
        vectorName: String,
        color: String?,
        @DrawableRes default: Int = R.drawable.ic_question_mark
): Drawable? {
    // Get the drawable from the registry. or return default
    val resource = VectorRegistry(vectorName)
    if (resource == null) {
        Log.e(TAG, "Failed to load vector in the registry: $vectorName")
        return this.getDrawableCompat(default)
    }

    // Base vectors are cached, retrieve it, then color it
    val baseVector = vectorCache.getOrPut(vectorName) {
        VectorMasterDrawable(this, resource)
    }

    // If color is null, render white. If color is invalid, warn and set to "undefined".
    // Setting the default in params will not make it white if null is explicitly passed.
    // If a color is undefined, it'll use a cached value randomly. This is an error scenario.
    val colorRegistryValue = ColorRegistry(color ?: "White")
    if (colorRegistryValue == null) {
        Log.w(TAG, "Color registry value does not exist: $color")
    }

    // Color the vector (if applicable)
    val modifiedVector = when (colorRegistryValue) {
        // if no color, do nothing (undefined color)
        null -> baseVector

        else -> {
            val colorValue = ContextCompat.getColor(this, colorRegistryValue)
            ColoredVectorDrawable(vectorName, baseVector, colorValue)
        }
    }

    // Return a version that caches draw results
    return CachedDrawable(modifiedVector, key="$vectorName-$color")
}