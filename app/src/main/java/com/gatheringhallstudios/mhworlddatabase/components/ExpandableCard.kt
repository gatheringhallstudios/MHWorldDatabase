package com.gatheringhallstudios.mhworlddatabase.components

import android.content.Context
import android.graphics.drawable.Animatable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.features.armor.list.compatSwitchVector
import com.gatheringhallstudios.mhworlddatabase.util.elevationToAlpha
import kotlinx.android.synthetic.main.cell_expandable_cardview.view.*

/**
 * A CardView with a space for a static header and expandable body.
 */
class ExpandableCardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    private var expandAnimationDuration = 300 //Should be shorter than the 180 of the arrow
    private var onExpand: () -> Unit = {}
    private var onContract: () -> Unit = {}
    private var cardElevation: Float = 0f
    private var headerLayout: Int = 0
    private var bodyLayout: Int = 0
    private var showRipple: Boolean = true

    private enum class CardState {
        EXPANDING,
        COLLAPSING
    }

    init {
        val inflater = getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.cell_expandable_cardview, this, true)
        card_arrow.setOnClickListener {
            toggle()
        }
        card_body.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            card_body.measure(MATCH_PARENT, WRAP_CONTENT)
            if (card_body.measuredHeight <= 0) {
                card_arrow.visibility = View.INVISIBLE
            } else {
                card_arrow.visibility = View.VISIBLE
            }
        }

        if (attrs != null) {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.ExpandableCardView)
            cardElevation = attributes.getFloat(R.styleable.ExpandableCardView_cardViewElevation, 0f)
            showRipple = attributes.getBoolean(R.styleable.ExpandableCardView_clickable, true)
            headerLayout = attributes.getResourceId(R.styleable.ExpandableCardView_cardHeaderLayout, R.layout.view_base_header_expandable_cardview)
            bodyLayout = attributes.getResourceId(R.styleable.ExpandableCardView_cardBodyLayout, R.layout.view_base_body_expandable_cardview)

            if (Build.VERSION.SDK_INT < 21) {
                card_container.cardElevation = cardElevation
            } else {
                card_container.elevation = cardElevation
            }
            card_overlay.alpha = elevationToAlpha(cardElevation.toInt())
            card_container.isClickable = showRipple
            card_container.isFocusable = showRipple
            setHeader(headerLayout)
            setBody(bodyLayout)
            attributes.recycle()
        }
    }

    fun setHeader(layout: Int) {
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        card_header.removeAllViews()
        card_header.addView(inflater.inflate(layout, this, false))
    }

    fun setBody(layout: Int) {
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        card_body.removeAllViews()
        card_body.addView(inflater.inflate(layout, this, false))
    }

    fun setOnClick(onClick: () -> Unit) {
        card_container.setOnClickListener {
            onClick()
        }
    }

    fun setOnExpand(onExpand: () -> Unit) {
        this.onExpand = onExpand
    }

    fun setOnContract(onContract: () -> Unit) {
        this.onContract = onContract
    }

    fun setCardElevation(cardElevation: Float) {
        card_container.cardElevation = cardElevation
        card_overlay.alpha = elevationToAlpha(cardElevation.toInt())
    }

    fun toggle() {
        card_body.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        if (card_body.measuredHeight == 0) return

        val initialHeight = card_container.height
        val headerHeight = card_header.height
        card_layout.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val targetHeight: Int = if (initialHeight == headerHeight) card_layout.measuredHeight else headerHeight
        if (targetHeight - initialHeight > 0) {
            animateViews(initialHeight,
                    targetHeight - initialHeight,
                    CardState.EXPANDING, card_container)
            onExpand()
        } else {
            animateViews(initialHeight,
                    initialHeight - targetHeight,
                    CardState.COLLAPSING, card_container)
            onContract()
        }
    }

    private fun animateViews(initialHeight: Int, distance: Int, animationType: CardState, cardView: View) {
        val expandAnimation = object : Animation() {
            var arrowStarted = false
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                cardView.layoutParams.height = if (animationType == CardState.EXPANDING)
                    (initialHeight + distance * interpolatedTime).toInt()
                else
                    (initialHeight - distance * interpolatedTime).toInt()

                cardView.requestLayout()
                if (!arrowStarted) {
                    arrowStarted = true
                    (cardView.card_arrow.drawable as Animatable).start()
                }
            }
        }

        expandAnimation.duration = expandAnimationDuration.toLong()
        cardView.startAnimation(expandAnimation)
        cardView.card_arrow.setImageResource(when (animationType) {
            CardState.EXPANDING -> compatSwitchVector(R.drawable.ic_expand_more_animated, R.drawable.ic_expand_more)
            CardState.COLLAPSING -> compatSwitchVector(R.drawable.ic_expand_less_animated, R.drawable.ic_expand_less)
        })
    }
}