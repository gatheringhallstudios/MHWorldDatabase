package com.gatheringhallstudios.mhworlddatabase.components

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R

/**
 * Custom view used to render a "skill points meter"
 */
class SkillLevelView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {
    companion object {
        const val TAG = "SkillLevelView"
    }

    /**
     * The current number of enabled skill points
     */
    var level = 0
        set(value) {
            field = value
            invalidate()
        }

    /**
     * The maximum number of possible skill points for this meter.
     * The max possible is 7
     */
    var maxLevel = 7
        set(value) {
            field = value
            invalidate()
        }

    init {
        if (attrs != null) {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.SkillLevelView)
            try {
                level = attributes.getInteger(R.styleable.SkillLevelView_level, 0)
                maxLevel = attributes.getInteger(R.styleable.SkillLevelView_maxLevel, 7)
            } finally {
                attributes.recycle()
            }
        }
    }

    private val maxSkillPoints = 7
    private val aspectRatio = maxSkillPoints + 2 // width scaling

    private val leftImg = ContextCompat.getDrawable(context, R.drawable.ui_skill_left)
    private val rightImg = ContextCompat.getDrawable(context, R.drawable.ui_skill_right)
    private val filledImg = ContextCompat.getDrawable(context, R.drawable.ui_skill_filled)
    private val emptyImg = ContextCompat.getDrawable(context, R.drawable.ui_skill_empty)
    private val gapImg = ContextCompat.getDrawable(context, R.drawable.ui_skill_space)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d(TAG, "Measured height: $heightMeasureSpec")
        val contentHeight = heightMeasureSpec - paddingTop - paddingBottom
        val newWidth = (aspectRatio * contentHeight) + paddingLeft + paddingRight
        setMeasuredDimension(newWidth, heightMeasureSpec)
    }

    public override fun onDraw(canvas: Canvas) {
        val x = paddingLeft
        val y = paddingTop

        // Draw the left skill border
        val blockSize = height - paddingTop - paddingBottom
        leftImg?.setBounds(x, y, blockSize, blockSize)
        leftImg?.draw(canvas)

        // Draw the squares inside the meter
        val startX = x + blockSize
        for (i in 0 until maxSkillPoints) {
            val image = when {
                i >= maxLevel -> gapImg
                i >= level -> emptyImg
                else -> filledImg
            }

            val x = (blockSize * i) + startX
            image?.setBounds(x, y, x + blockSize, y + blockSize)
            image?.draw(canvas)
        }

        // Draw the right skill border
        val rightEdge = width - paddingRight
        rightImg?.setBounds(rightEdge - blockSize, 0, rightEdge, blockSize)
        rightImg?.draw(canvas)
    }
}