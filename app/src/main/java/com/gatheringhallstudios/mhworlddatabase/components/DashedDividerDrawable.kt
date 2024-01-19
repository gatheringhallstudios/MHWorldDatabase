package com.gatheringhallstudios.mhworlddatabase.components

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.util.dpToPx

/**
 * A custom drawable used to draw dashed dividers.
 * This is used because XML Line drawables do not support strokes for lines when hardware acceleration is enabled.
 * The current XML version uses a workaround with a rectangle,
 * but its generally safer and better performance to use this one when possible.
 */
class DashedDividerDrawable(context: Context): Drawable() {
    val color = ContextCompat.getColor(context, R.color.itemDividerColor)

    val dashWidth = dpToPx(10)
    val dashGap = dpToPx(5)

    override fun getIntrinsicHeight(): Int {
        return dpToPx(1)
    }

    override fun draw(canvas: Canvas) {
        val effect = DashPathEffect(floatArrayOf(dashWidth.toFloat(), dashGap.toFloat()), 0F)
        val paint = Paint()
        paint.pathEffect = effect
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = intrinsicHeight.toFloat()
        paint.color = color

        val y = bounds.centerY().toFloat()

        val path = Path()
        path.moveTo(bounds.left.toFloat(), y)
        path.lineTo(bounds.right.toFloat(), y)

        canvas.drawPath(path, paint)
    }

    override fun setAlpha(alpha: Int) {

    }

    override fun getOpacity(): Int {
        // if alpha is 255, return OPAQUE instead
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

}