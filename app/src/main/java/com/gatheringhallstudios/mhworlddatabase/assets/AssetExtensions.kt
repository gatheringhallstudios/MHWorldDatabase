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


/**
 * Extension: Receives an asset loader for a fragment
 */
val Fragment.assetLoader get() = AssetLoader(context!!)

/**
 * Extension: Receives an asset loader for a view
 */
val View.assetLoader get() = AssetLoader(context!!)

/**
 * Extension: Receives an asset loader for this context.
 */
val Context.assetLoader get() = AssetLoader(this)

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

    val colorRegistryValue = when (color) {
        null -> null
        else -> ColorRegistry(color)
    }

    // Color the vector (if applicable)
    val modifiedVector = when {
        // if no color, do nothing
        color == null -> baseVector

        // if invalid color, log and do nothing
        colorRegistryValue == null -> {
            Log.w(TAG, "Color registry value does not exist: $color")
            baseVector
        }

        else -> {
            val colorValue = ContextCompat.getColor(this, colorRegistryValue)
            ColoredVectorDrawable(vectorName, baseVector, colorValue)
        }
    }

    // Return a version that caches draw results
    return CachedDrawable(modifiedVector)
}