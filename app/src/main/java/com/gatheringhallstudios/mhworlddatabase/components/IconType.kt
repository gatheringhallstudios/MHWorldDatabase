@file:JvmName("IconTypeFn")
package com.gatheringhallstudios.mhworlddatabase.components

import android.widget.ImageView
import com.gatheringhallstudios.mhworlddatabase.R

/**
 * Defines the decoration to apply to an icon.
 */
enum class IconType {
    NORMAL,
    PAPER,
    EMBELLISHED,
    ZEMBELLISHED
}


/**
 * Extension that applies the icon type to an image.
 * Does not modify padding, so its only a rough transformation
 * TODO: Move somewhere more sane
 * TODO: Handle padding somehow. Perhaps a rough 2dp for all but NORMAL and 0dp for normal suffices?
 */
fun ImageView.applyIconType(type: IconType) {
    val background = when (type) {
        IconType.NORMAL -> 0
        IconType.PAPER -> R.drawable.bg_icon_decorator_paper
        IconType.EMBELLISHED -> R.drawable.bg_icon_decorator
        IconType.ZEMBELLISHED -> R.drawable.ic_decorator_zembelish
    }

    if (background == 0) {
        this.background = null
    } else {
        this.setBackgroundResource(background)
    }
}