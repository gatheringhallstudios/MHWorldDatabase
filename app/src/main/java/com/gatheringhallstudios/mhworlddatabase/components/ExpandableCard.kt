package com.gatheringhallstudios.mhworlddatabase.components

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Animatable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.features.armor.list.compatSwitchVector
import com.gatheringhallstudios.mhworlddatabase.util.elevationToAlpha
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import kotlinx.android.synthetic.main.cell_expandable_cardview.view.*
import kotlin.math.abs

private const val clickThreshold = 50
private const val threshold = 700

/**
 * A CardView with a space for a static header and expandable body.
 */
class ExpandableCardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    private var expandAnimationDuration = 300
    private var swipeReboundAnimationDuration = 200

    private var cardElevation: Float = 0f
    private var headerLayout: Int = 0
    private var bodyLayout: Int = 0
    private var showRipple: Boolean = true
    private var swipeLeftEnabled = false
    private var swipeRightEnabled = false
    private var swipeLeftIcon: Int = android.R.drawable.ic_menu_delete
    private var swipeRightIcon: Int = android.R.drawable.ic_input_add
    private var swipeLeftBackground: Int = Color.parseColor("#FF1744")
    private var swipeRightBackground: Int = Color.parseColor( "#00E676")
    private var cardState: CardState = CardState.COLLAPSED

    private var onSwipeLeft: () -> Unit = {}
    private var onSwipeRight: () -> Unit = {}
    private var onClick: () -> Unit = {}
    private var onExpand: () -> Unit = {}
    private var onContract: () -> Unit = {}

    enum class CardState {
        EXPANDED,
        EXPANDING,
        COLLAPSED,
        COLLAPSING
    }

    private enum class CardAnimation {
        EXPANDING,
        COLLAPSING
    }

    init {
        val inflater = getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.cell_expandable_cardview, this, true)

        if (attrs != null) {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.ExpandableCardView)
            cardElevation = attributes.getFloat(R.styleable.ExpandableCardView_cardViewElevation, 0f)
            showRipple = attributes.getBoolean(R.styleable.ExpandableCardView_clickable, true)
            headerLayout = attributes.getResourceId(R.styleable.ExpandableCardView_cardHeaderLayout, R.layout.view_workshop_header_expandable_cardview_base)
            bodyLayout = attributes.getResourceId(R.styleable.ExpandableCardView_cardBodyLayout, R.layout.view_workshop_body_cardview_base)
            expandAnimationDuration = attributes.getInt(R.styleable.ExpandableCardView_expandAnimationDuration, 300)
            swipeReboundAnimationDuration = attributes.getInt(R.styleable.ExpandableCardView_swipeReboundDuration, 200)
            swipeLeftIcon = attributes.getResourceId(R.styleable.ExpandableCardView_swipeLeftIcon, android.R.drawable.ic_menu_delete)
            swipeLeftBackground = attributes.getColor(R.styleable.ExpandableCardView_swipeLeftBackground, swipeLeftBackground)
            swipeRightIcon = attributes.getResourceId(R.styleable.ExpandableCardView_swipeRightIcon, android.R.drawable.ic_input_add)
            swipeRightBackground = attributes.getColor(R.styleable.ExpandableCardView_swipeRightBackground, swipeRightBackground)

            val swipeMode = attributes.getInt(R.styleable.ExpandableCardView_swipeMode, 0)
            swipeLeftEnabled = swipeMode == 1 || swipeMode == 3
            swipeRightEnabled = swipeMode == 2 || swipeMode == 3

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
            this.cardState = CardState.COLLAPSED
            attributes.recycle()
        }

        setLeftLayout(swipeLeftIcon, swipeLeftBackground)
        setRightLayout(swipeRightIcon, swipeRightBackground)
        card_body.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            card_body.measure(MATCH_PARENT, WRAP_CONTENT)
            if (card_body.measuredHeight <= 0) {
                card_arrow.alpha = 0.3f
            } else {
                card_arrow.alpha = 1.0f
            }

            if (cardState == CardState.EXPANDED) {
                val initialHeight = card_container.height
                card_body.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                val targetHeight: Int = card_layout.measuredHeight

                if (targetHeight - initialHeight > 0) {
                    cardState = CardState.EXPANDING
                    animateViews(initialHeight,
                            targetHeight - initialHeight,
                            CardAnimation.EXPANDING, card_container, false)
                } else if (targetHeight - initialHeight < 0) {
                    cardState = CardState.COLLAPSING
                    animateViews(initialHeight,
                            initialHeight - targetHeight,
                            CardAnimation.COLLAPSING, card_container, false)
                }
            }
        }

        //Swipe/onclick handler
        card_container.setOnTouchListener(OnSwipeTouchListener(card_layout, left_icon_layout, right_icon_layout, context,
                this.onSwipeLeft, this.onSwipeRight, this.onClick, this.swipeReboundAnimationDuration, this.swipeLeftEnabled, this.swipeRightEnabled))

        findViewById<LinearLayout>(R.id.card_layout).post {
            val delegateTouchArea = Rect()
            val cardArrow = findViewById<ImageButton>(R.id.card_arrow).apply {
                setOnClickListener {
                    toggle()
                }
                getHitRect(delegateTouchArea)
            }

            delegateTouchArea.right += 100
            delegateTouchArea.left -= 100
            delegateTouchArea.top -= 100
            delegateTouchArea.bottom += 100
            (cardArrow.parent as? View)?.apply {
                touchDelegate = TouchDelegate(delegateTouchArea, cardArrow)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        resetState()
    }

    /**
     * Sets the card state to either full expanded or full collapsed immediately without the animation
     */
    fun setCardState(cardState: CardState) {
        when (cardState) {
            CardState.COLLAPSING, CardState.COLLAPSED -> {
                this.cardState = CardState.COLLAPSED
                val headerParams = card_header.layoutParams
                //Using the layout params instead of the height param because the height may not yet be set when setCardState is called.
                card_container.layoutParams = card_container.layoutParams.apply {
                    height = headerParams.height
                }
                card_arrow.setImageResource(compatSwitchVector(R.drawable.ic_expand_less_animated, R.drawable.ic_expand_less))
            }
            CardState.EXPANDING, CardState.EXPANDED -> {
                card_layout.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                card_container.layoutParams.height = card_layout.measuredHeight
                this.cardState = CardState.EXPANDED
                card_arrow.setImageResource(compatSwitchVector(R.drawable.ic_expand_more_animated, R.drawable.ic_expand_less))
            }
        }
    }

    fun setLeftLayout(reference: Int?, color: Int?) {
        if (reference != null) left_icon.setImageDrawable(context.getDrawableCompat(reference))
        if (color != null) left_icon_layout.setBackgroundColor(color)
    }

    fun setRightLayout(reference: Int?, color: Int?) {
        if (reference != null) right_icon.setImageDrawable(context.getDrawableCompat(reference))
        if (color != null) right_icon_layout.setBackgroundColor(color)
    }

    fun setHeader(layout: Int) {
        card_header.removeAllViews()
        val view = LayoutInflater.from(this.context).inflate(layout, card_header, false)
        card_header.addView(view)
        val inflatedHeight = view.layoutParams.height
        card_header.layoutParams = card_header.layoutParams.apply {
            height = inflatedHeight
        }

        //Only adjust the size of the card container if the card is not in it's EXPANDED state
        //When the card is in EXPANDED state, the height is WRAP_CONTENT so it will automatically adjust to match the new header
        if (this.cardState == CardState.COLLAPSED || this.cardState == CardState.COLLAPSING) card_container.layoutParams = card_container.layoutParams.apply {
            height = inflatedHeight
        }
    }

    fun setBody(layout: Int) {
        card_body.removeAllViews()
        if (layout == 0) return
        LayoutInflater.from(this.context).inflate(layout, card_body, true)
    }

    fun setOnClick(onClick: () -> Unit) {
        this.onClick = onClick
        //Update Swipe/onclick handler
        card_container.setOnTouchListener(OnSwipeTouchListener(card_layout, left_icon_layout, right_icon_layout, context,
                this.onSwipeLeft, this.onSwipeRight, this.onClick, this.swipeReboundAnimationDuration, this.swipeLeftEnabled, this.swipeRightEnabled))
    }

    fun setOnSwipeLeft(onSwipeLeft: () -> Unit) {
        this.onSwipeLeft = onSwipeLeft
        this.swipeLeftEnabled = true
        //Update Swipe/onclick handler
        card_container.setOnTouchListener(OnSwipeTouchListener(card_layout, left_icon_layout, right_icon_layout, context,
                this.onSwipeLeft, this.onSwipeRight, this.onClick, this.swipeReboundAnimationDuration, this.swipeLeftEnabled, this.swipeRightEnabled))
    }

    fun setOnSwipeRight(onSwipeRight: () -> Unit) {
        this.onSwipeRight = onSwipeRight
        this.swipeRightEnabled = true
        //Update Swipe/onclick handler
        card_container.setOnTouchListener(OnSwipeTouchListener(card_layout, left_icon_layout, right_icon_layout, context,
                this.onSwipeLeft, this.onSwipeRight, this.onClick, this.swipeReboundAnimationDuration, this.swipeLeftEnabled, this.swipeRightEnabled))
    }

    fun resetState() {
        val layoutParams = left_icon_layout.layoutParams
        layoutParams.width = 0
        left_icon_layout.layoutParams = layoutParams

        card_layout.x = 0f

        val layoutParams3 = right_icon_layout.layoutParams
        layoutParams3.width = 0
        right_icon_layout.layoutParams = layoutParams3

        if (cardState == CardState.EXPANDED) {
            val cardLayoutParams = card_container.layoutParams
            cardLayoutParams.height = card_header.height
            card_container.layoutParams = cardLayoutParams
            card_arrow.setImageResource(compatSwitchVector(R.drawable.ic_expand_less_animated, R.drawable.ic_expand_less))
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
        cardState = if (cardState == CardState.COLLAPSED) CardState.EXPANDING else CardState.COLLAPSING
        card_body.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        if (card_body.measuredHeight == 0) return

        val initialHeight = card_container.height
        val headerHeight = card_header.height
        card_layout.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val targetHeight: Int = if (initialHeight == headerHeight) card_layout.measuredHeight else headerHeight
        if (targetHeight - initialHeight > 0) {
            animateViews(initialHeight,
                    targetHeight - initialHeight,
                    CardAnimation.EXPANDING, card_container)
            onExpand()
            card_arrow.setImageResource(compatSwitchVector(R.drawable.ic_expand_more_animated, R.drawable.ic_expand_more))
        } else {
            animateViews(initialHeight,
                    initialHeight - targetHeight,
                    CardAnimation.COLLAPSING, card_container)
            onContract()
            card_arrow.setImageResource(compatSwitchVector(R.drawable.ic_expand_less_animated, R.drawable.ic_expand_less))
        }
    }

    private fun animateViews(initialHeight: Int, distance: Int, animationType: CardAnimation, cardView: View, animateArrow: Boolean = true) {
        val expandAnimation = object : Animation() {
            var arrowStarted = false
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                cardView.layoutParams.height = if (animationType == CardAnimation.EXPANDING)
                    (initialHeight + distance * interpolatedTime).toInt()
                else
                    (initialHeight - distance * interpolatedTime).toInt()

                cardView.requestLayout()
                if (!arrowStarted && animateArrow) {
                    arrowStarted = true
                    (cardView.card_arrow.drawable as Animatable).start()
                }
            }
        }

        expandAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                if (!animateArrow) {
                    cardState = if (cardState == CardState.EXPANDING) CardState.COLLAPSED else CardState.EXPANDED
                } else {
                    cardState = if (cardState == CardState.EXPANDING) CardState.EXPANDED else CardState.COLLAPSED
                }
            }
        })

        expandAnimation.duration = expandAnimationDuration.toLong()
        cardView.startAnimation(expandAnimation)
    }

    class OnSwipeTouchListener(val view: LinearLayout, val left_view: LinearLayout,
                               val right_view: LinearLayout, val ctx: Context,
                               val onSwipeLeft: () -> Unit, val onSwipeRight: () -> Unit,
                               val onClick: () -> Unit,
                               val reboundAnimationDuration: Int,
                               val swipeLeftEnabled: Boolean, val swipeRightEnabled: Boolean) : OnTouchListener {
        var initialX = 0f
        var dx = 0f
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = event.x
                    dx = 0f
                }
                MotionEvent.ACTION_MOVE -> {
                    dx = event.x - initialX
                    if (dx > 0 && dx < threshold && swipeRightEnabled) {
                        val layoutParams = left_view.layoutParams
                        layoutParams.width = dx.toInt()
                        left_view.layoutParams = layoutParams
                        view.x = dx
                    } else if (dx < 0 && dx > -threshold && swipeLeftEnabled) {
                        val layoutParams = right_view.layoutParams
                        layoutParams.width = -1 * dx.toInt()
                        right_view.layoutParams = layoutParams
                        view.x = dx
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (abs(dx) < clickThreshold) onClick()
                    else if (swipeLeftEnabled || swipeRightEnabled) animateViews(dx.toInt(), (-1 * dx).toInt(), onSwipeLeft, onSwipeRight)
                }
                MotionEvent.ACTION_CANCEL -> {
                    if (swipeLeftEnabled || swipeRightEnabled) animateViews(dx.toInt(), (-1 * dx).toInt(), onSwipeLeft, onSwipeRight)
                }
            }
            return false
        }

        private fun animateViews(initialX: Int, distance: Int, onSwipeLeft: () -> Unit, onSwipeRight: () -> Unit) {
            val reboundAnimation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    if (distance < 0 && swipeRightEnabled) {
                        view.x = initialX + distance * interpolatedTime
                        left_view.layoutParams.width = (initialX + distance * interpolatedTime).toInt()
                    } else if (distance > 0 && swipeLeftEnabled) {
                        view.x = initialX + distance * interpolatedTime
                        right_view.layoutParams.width = -1 * (initialX + distance * interpolatedTime).toInt()
                    }
                    left_view.requestLayout()
                    right_view.requestLayout()
                }
            }

            reboundAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    if (distance < -175 && swipeRightEnabled) {
                        onSwipeRight()
                    } else if (distance > 175 && swipeLeftEnabled) {
                        onSwipeLeft()
                    }
                }
            })

            reboundAnimation.duration = reboundAnimationDuration.toLong()
            left_view.startAnimation(reboundAnimation)
        }
    }
}

