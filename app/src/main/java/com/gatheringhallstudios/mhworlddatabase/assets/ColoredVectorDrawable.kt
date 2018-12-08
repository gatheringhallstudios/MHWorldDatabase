package com.gatheringhallstudios.mhworlddatabase.assets

import android.graphics.Canvas
import androidx.annotation.ColorInt
import android.util.Log
import com.gatheringhallstudios.mhworlddatabase.util.DrawableWrapper
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable
import com.sdsmdg.harjot.vectormaster.models.PathModel


const val PATH_NAME = "base"

/**
 * A wrapper over VectorMasterDrawable to change the base path's fill color
 */
class ColoredVectorDrawable(
        /** Used for error message */
        private val name: String,

        private val vector: VectorMasterDrawable,
        @ColorInt private val color: Int
): DrawableWrapper(vector) {
    private val basePath: PathModel? by lazy {
        val result = vector.getPathModelByName(PATH_NAME)
        if (result == null) {
            Log.w(TAG, "Could not find path $PATH_NAME in vector $name")
        }
        result
    }

    override fun draw(canvas: Canvas?) {
        basePath?.fillColor = color
        super.draw(canvas)
    }
}