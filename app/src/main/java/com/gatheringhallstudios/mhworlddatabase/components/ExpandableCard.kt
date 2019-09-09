package com.gatheringhallstudios.mhworlddatabase.components

import android.content.Context
import android.graphics.drawable.Animatable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.features.armor.list.compatSwitchVector
import com.gatheringhallstudios.mhworlddatabase.util.ConvertElevationToAlphaConvert
import kotlinx.android.synthetic.main.cell_expandable_cardview.view.*


class ExpandableCardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    private var rowHeight: Int = 189 //Magic height of the row with the margins included
    private var expandAnimationDuration = 100 //Should be shorter than the 180 of the arrow
    private var onExpand: () -> Unit = {}
    private var onContract: () -> Unit = {}
    private var cardElevation: Float = 0f
    private var headerLayout: Int = 0
    private var bodyLayout: Int = 0
    private var showRipple: Boolean = true

    private enum class cardState {
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

        if (attrs != null) {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.ExpandableCardView)
            cardElevation = attributes.getFloat(R.styleable.ExpandableCardView_cardViewElevation, 0f)
            showRipple = attributes.getBoolean(R.styleable.ExpandableCardView_clickable, true)
            headerLayout = attributes.getResourceId(R.styleable.ExpandableCardView_cardHeaderLayout, R.layout.view_base_header_expandable_cardview)
            bodyLayout = attributes.getResourceId(R.styleable.ExpandableCardView_cardBodyLayout, R.layout.view_base_body_expandable_cardview)


            if(Build.VERSION.SDK_INT < 21 ) {
                card_container.cardElevation = cardElevation
            } else {
                card_container.elevation = cardElevation
            }
            card_overlay.alpha = ConvertElevationToAlphaConvert(cardElevation.toInt())
            card_container.isClickable = showRipple
            card_container.isFocusable = showRipple
            setHeader(headerLayout)
            setBody(bodyLayout)
            attributes.recycle()
        }
    }

    fun setHeader(layout: Int) {
        val inflater = getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        card_header.removeAllViews()
        card_header.addView(inflater.inflate(layout, this, false))
    }

    fun setBody(layout: Int) {
        val inflater = getContext()
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

//    fun setHeight(height: Int) {
//        // Gets the layout params that will allow you to resize the layout
//        val params: ViewGroup.LayoutParams = header_layout.layoutParams
//        params.height = height
//        header_layout.layoutParams = params
//    }

    fun setCardElevation(cardElevation: Float) {
        card_container.cardElevation = cardElevation
        card_overlay.alpha = ConvertElevationToAlphaConvert(cardElevation.toInt())
    }

    fun toggle() {
        val initialHeight = card_container.height
        card_layout.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val targetHeight: Int = if (initialHeight == rowHeight) card_layout.measuredHeight else rowHeight
        if (targetHeight - initialHeight > 0) {
            animateViews(initialHeight,
                    targetHeight - initialHeight,
                    cardState.EXPANDING, card_container)
            onExpand()
        } else {
            animateViews(initialHeight,
                    initialHeight - targetHeight,
                    cardState.COLLAPSING, card_container)
            onContract()
        }
    }

    private fun animateViews(initialHeight: Int, distance: Int, animationType: cardState, cardView: View) {
        val expandAnimation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                cardView.layoutParams.height = if (animationType == cardState.EXPANDING)
                    (initialHeight + distance * interpolatedTime).toInt()
                else
                    (initialHeight - distance * interpolatedTime).toInt()

                cardView.layoutParams.height = if (animationType == cardState.EXPANDING)
                    (initialHeight + distance * interpolatedTime).toInt()
                else
                    (initialHeight - distance * interpolatedTime).toInt()
                cardView.requestLayout()

                if (animationType == cardState.EXPANDING) {
                    val drawable = cardView.card_arrow.drawable
                    if (drawable is Animatable) {
                        drawable.start()
                        val params = card_container.layoutParams
                        params.height = LayoutParams.WRAP_CONTENT
                        card_container.layoutParams = params
                    }
                } else {
                    val drawable = cardView.card_arrow.drawable
                    if (drawable is Animatable) {
                        drawable.start()
                    }
                }
            }
        }

        expandAnimation.duration = expandAnimationDuration.toLong()
        cardView.startAnimation(expandAnimation)
        cardView.card_arrow.setImageResource(when (animationType) {
            cardState.EXPANDING -> compatSwitchVector(R.drawable.ic_expand_more_animated, R.drawable.ic_expand_more)
            cardState.COLLAPSING -> compatSwitchVector(R.drawable.ic_expand_less_animated, R.drawable.ic_expand_less)
        })
    }
}