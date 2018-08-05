package com.gatheringhallstudios.mhworlddatabase.components

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * A custom view with the DashedDividerDrawable built in.
 * Custom drawables cannot be used in XML, so this is a compromise
 */
class DashedDividerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {
    init {
        background = DashedDividerDrawable(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, background.intrinsicHeight)
    }
}