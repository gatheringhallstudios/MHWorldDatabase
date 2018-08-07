package com.gatheringhallstudios.mhworlddatabase.components

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.GroupAdapter

/**
 * Defines a base divider for vertical RecyclerViews.
 */
abstract class BaseVerticalDivider: RecyclerView.ItemDecoration() {
    /**
     * Returns true if a divider should be drawn between the two views
     */
    abstract fun shouldDraw(parent: RecyclerView, firstView: View, firstIdx: Int, secondView: View, secondIdx: Int): Boolean

    /**
     * Performs the actual draw at the left/right coordinate (adjusted for padding)
     * and a y coordinate. This y coordinate should update to reflect the intrinsic height.
     */
    abstract fun performDraw(canvas: Canvas, left: Int, right: Int, yCoord: Int)

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        canvas.save()

        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        for (i in 0 until parent.childCount - 1) {
            val first = parent.getChildAt(i)
            val second = parent.getChildAt(i + 1)

            if (shouldDraw(parent, first, i, second, i+1)) {
                val mBounds = Rect()
                parent.getDecoratedBoundsWithMargins(first, mBounds)
                val bottom = mBounds.bottom + Math.round(first.translationY)

                performDraw(canvas, left, right, bottom)
            }
        }

        canvas.restore()
    }
}

/**
 * Defines a recyclerview item decoration that draws between all items, using a provided drawable.
 * Similar to use the built in DividerItemDecoration, but defaults to vertical and takes a drawable in the constructor.
 */
class StandardDivider(private val drawable: Drawable?): BaseVerticalDivider() {
    override fun shouldDraw(parent: RecyclerView, firstView: View, firstIdx: Int, secondView: View, secondIdx: Int): Boolean {
        return true
    }

    override fun performDraw(canvas: Canvas, left: Int, right: Int, yCoord: Int) {
        drawable?.let {
            val top = yCoord - drawable.intrinsicHeight
            drawable.setBounds(left, top, right, yCoord)
            drawable.draw(canvas)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        drawable?.let {
            outRect.bottom = drawable.intrinsicHeight
        }
    }
}

/**
 * Defines an recycler view item decoration that separates between groupie header items
 * but not detail items
 */
class HeaderItemDivider(private val drawable: Drawable): BaseVerticalDivider() {
    override fun shouldDraw(parent: RecyclerView, firstView: View, firstIdx: Int, secondView: View, secondIdx: Int): Boolean {
        val adapter = parent.adapter as? GroupAdapter
        if (adapter == null) {
            Log.e("HeaderItemDivider", "HeaderItemDivider only works for GroupAdapters")
            return false
        }

        // these should draw if the secondview is a header.
        val secondAdapterIdx = parent.getChildAdapterPosition(secondView)
        if (secondAdapterIdx == RecyclerView.NO_POSITION) {
            return false // view doesn't exist (aka collapsing), so don't draw
        }

        return adapter.getItem(secondAdapterIdx) is ExpandableItem
    }

    override fun performDraw(canvas: Canvas, left: Int, right: Int, yCoord: Int) {
        val top = yCoord - drawable.intrinsicHeight
        drawable.setBounds(left, top, right, yCoord)
        drawable.draw(canvas)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = drawable.intrinsicHeight
    }
}

/**
 * A divider that separates "child" items
 */
class ChildDivider(private val drawable: Drawable?): BaseVerticalDivider() {
    override fun shouldDraw(parent: RecyclerView, firstView: View, firstIdx: Int, secondView: View, secondIdx: Int): Boolean {
        val firstIsHeader = firstView.getTag(R.id.view_is_header)
        val secondIsHeader = secondView.getTag(R.id.view_is_header)

        // if either child or nextChild is a "header", should not draw
        if (firstIsHeader == true || secondIsHeader == true) {
            return false
        }

        return true
    }

    override fun performDraw(canvas: Canvas, left: Int, right: Int, yCoord: Int) {
        drawable?.let {
            val top = yCoord - drawable.intrinsicHeight
            drawable.setBounds(left, top, right, yCoord)
            drawable.draw(canvas)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        drawable?.let {
            outRect.bottom = drawable.intrinsicHeight
        }
    }
}
