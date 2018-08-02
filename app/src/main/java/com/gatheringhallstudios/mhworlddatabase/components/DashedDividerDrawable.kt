package com.gatheringhallstudios.mhworlddatabase.components

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.gatheringhallstudios.mhworlddatabase.R

fun dpToPx(dp: Int) =  (dp * Resources.getSystem().displayMetrics.density).toInt()

class DashedDividerDrawable(context: Context): Drawable() {
    val color = ContextCompat.getColor(context, R.color.itemDividerColor)

    val dashWidth = dpToPx(10)
    val dashGap = dpToPx(5)

    override fun getIntrinsicHeight(): Int {
        return dpToPx(1)
    }

    override fun draw(canvas: Canvas?) {
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

        canvas?.drawPath(path, paint)
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