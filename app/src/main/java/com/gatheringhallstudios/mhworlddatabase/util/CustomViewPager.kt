package com.gatheringhallstudios.mhworlddatabase.util

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


class CustomViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewPager(context, attrs) {
    private var disable: Boolean = false

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return (!disable && super.onInterceptTouchEvent(event))
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return (!disable) && super.onTouchEvent(event)
    }

    fun disableScroll(disable: Boolean) {
        this.disable = disable
    }
}