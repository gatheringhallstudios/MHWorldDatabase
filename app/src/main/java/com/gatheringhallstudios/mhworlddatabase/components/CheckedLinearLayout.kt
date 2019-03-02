package com.gatheringhallstudios.mhworlddatabase.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import android.widget.LinearLayout

/**
 * Descendant of FrameLayout which implements the Checkable interface.
 * Use to implement custom toggle buttons.
 */
class CheckedLinearLayout : LinearLayout, Checkable {
    companion object {
        @JvmStatic
        val CheckedStateSet = intArrayOf(android.R.attr.state_checked)
    }

    private var checked: Boolean = false

    constructor(ctx: Context): super(ctx)
    constructor(ctx: Context, attrs: AttributeSet): super(ctx, attrs)
    constructor(ctx: Context, attrs: AttributeSet, defStyle: Int): super(ctx, attrs, defStyle)

    init {
        // by default, the click should toggle the checked state
        setOnClickListener { this.toggle() }
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     */
    var onCheckedChangeListener: ((checkable: Checkable, isChecked: Boolean) -> Unit)? = null

    override fun isChecked() = checked
    override fun setChecked(checked: Boolean) {
        this.checked = checked
        refreshDrawableState()

        onCheckedChangeListener?.invoke(this, this.isChecked)
    }

    override fun toggle() {
        this.isChecked = !this.isChecked
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked) {
            View.mergeDrawableStates(drawableState, CheckedStateSet)
        }
        return drawableState
    }
}