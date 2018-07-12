package com.gatheringhallstudios.mhworlddatabase.components

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.GroupAdapter

/**
 * Defines an recycler view item decoration that separates between header items
 * but not detail items
 */
class HeaderItemDecorator(private val mDivider: Drawable) : RecyclerView.ItemDecoration() {
    private fun childHasBorder(parent: RecyclerView, position: Int): Boolean {
        val adapter = parent.adapter as GroupAdapter

        val isAtEnd = position == (adapter.getItemCount() - 1)
        val nextItem = when (isAtEnd) {
                true -> null
                false -> adapter.getItem(position + 1)
        }

        return nextItem is ExpandableItem
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        canvas.save()

        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        for (i in 0 until parent.childCount - 1) {
            val child = parent.getChildAt(i)
            val trueIdx = parent.getChildAdapterPosition(child)

            if (trueIdx == RecyclerView.NO_POSITION || !childHasBorder(parent, trueIdx)) {
                continue
            }

            val mBounds = Rect()
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            val bottom = mBounds.bottom + Math.round(child.translationY)
            val top = bottom - mDivider.intrinsicHeight

            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(canvas)
        }
        canvas.restore()
    }
}