package com.gatheringhallstudios.mhworlddatabase.assets

import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.v4.util.LruCache
import com.gatheringhallstudios.mhworlddatabase.util.DrawableWrapper
import java.util.*

data class CacheKey(
        val id: String,
        val width: Int,
        val height: Int
)

private val cacheSizeKB = 8 * 1024 // 8MB

private val cache = object: LruCache<CacheKey, Bitmap>(cacheSizeKB) {
    override fun sizeOf(key: CacheKey?, value: Bitmap): Int {
        return value.byteCount / 1024 // return size in kilobytes
    }
}

/**
 * Extension: Gets a value from an LRUCache, or inserts and returns it
 */
inline fun <K, V> LruCache<K, V>.getOrPut(key: K, build: () -> V): V {
    val value: V? = this[key]
    if (value != null) {
        return value
    }

    // doesn't exist in cache, so build it
    val newValue = build()
    this.put(key, newValue)
    return newValue
}

/**
 * A wrapper over a drawable that caches the draw result to a bitmap,
 * and reuses the bitmap so long as it remains in the cache.
 */
class CachedDrawable(innerDrawable: Drawable) : DrawableWrapper(innerDrawable) {
    private val cacheId = UUID.randomUUID().toString()

    override fun draw(canvas: Canvas?) {
        if (canvas == null) return

        val width = canvas.width
        val height = canvas.height

        val key = CacheKey(cacheId, width, height)

        val cachedBitmap = cache.getOrPut(key) {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val subCanvas = Canvas(bitmap)
            wrappedDrawable.setBounds(0, 0, bitmap.width, bitmap.height)
            wrappedDrawable.draw(subCanvas)

            bitmap
        }

        canvas.drawBitmap(cachedBitmap, null, canvas.clipBounds, null)
    }
}