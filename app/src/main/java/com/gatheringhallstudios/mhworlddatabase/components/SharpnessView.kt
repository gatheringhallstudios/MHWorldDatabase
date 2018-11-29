package com.gatheringhallstudios.mhworlddatabase.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R

/**
 * Collection of colors
 */
private object SharpnessColors {
    val RED = Color.RED
    val YELLOW = Color.YELLOW
    val GREEN = Color.GREEN
    val ORANGE = Color.rgb(255, 150, 0)
    val BLUE = Color.rgb(20, 131, 208)
    val WHITE = Color.WHITE
    val PURPLE = Color.rgb(120, 81, 169)
}

/**
 * Draws a sharpness level by values
 *
 * Max sharpness units combined should not exceed the value of int maxSharpness
 */
class SharpnessView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    constructor(context: Context, sharpness: IntArray) : this(context, null, 0) {
        init(sharpness)
    }

    fun drawSharpness(values: IntArray) {
        init(values)
    }

    companion object {
        private val TAG = "DrawSharpness"
    }

    private var red: Int = 0
    private var orange: Int = 0
    private var yellow: Int = 0
    private var green: Int = 0
    private var blue: Int = 0
    private var white: Int = 0
    private var purple: Int = 0

    private var viewHeight: Int = 0
    private var viewWidth: Int = 0

    // Settings
    private val maxSharpness = 400
    private val marginScale = 8 // higher = smaller margins
    private val innerMargin = 3   //Margin between sharpeness bar measured in pixels

    private var paint = Paint()

    init {
        if (attrs != null) {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.SharpnessView)
            red = attributes.getInt(R.styleable.SharpnessView_red, 0)
            orange = attributes.getInt(R.styleable.SharpnessView_orange, 0)
            yellow = attributes.getInt(R.styleable.SharpnessView_yellow, 0)
            blue = attributes.getInt(R.styleable.SharpnessView_blue, 0)
            white = attributes.getInt(R.styleable.SharpnessView_white, 0)
            purple = attributes.getInt(R.styleable.SharpnessView_purple, 0)

            attributes.recycle()
        }
    }

    private fun init(sharpness: IntArray) {
        red = sharpness[0]
        orange = sharpness[1]
        yellow = sharpness[2]
        green = sharpness[3]
        blue = sharpness[4]
        white = sharpness[5]
        purple = sharpness[6]
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Width should be no greater than 500px
        // Height should be no greater than 50px
        val measuredWidth: Int = Math.min(500, View.MeasureSpec.getSize(widthMeasureSpec))
        val measuredHeight: Int = Math.min(60, View.MeasureSpec.getSize(heightMeasureSpec))

        // Store our width and height
        viewWidth = measuredWidth
        viewHeight = measuredHeight

        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Margins are defined by height/marginScale
        val margins = Math.floor((viewHeight / marginScale).toDouble()).toInt()

        // Scale factor is used to multiply sharpness values to make sure full sharpness fills the bar
        // Must be a float to retain accuracy until pixel conversion
        val scalefactor = (viewWidth - margins * 2).toFloat() / maxSharpness

        // specify the width of each bar
        val barWidth = (scalefactor * maxSharpness).toInt() + margins * 2

        // Draw the background
        paint.color = Color.BLACK
        paint.strokeWidth = 4f
        canvas.drawRect(0f, 0f, barWidth.toFloat(), viewHeight.toFloat(), paint)

        // Draw bar
        val barBottom = viewHeight - margins
        drawBar(canvas, margins, scalefactor, margins, barBottom,
                red, orange, yellow, green, blue, white, purple)
    }

    private fun drawBar(canvas: Canvas, margins: Int, scalefactor: Float, bartop: Int, barbottom: Int,
                        ired: Int, iorange: Int, iyellow: Int,
                        igreen: Int, iblue: Int, iwhite: Int, ipurple: Int) {

        // Run through the bar and accumulate sharpness
        paint.strokeWidth = 0f

        var start = margins

        // helper that draws the color for an amount, and progresses the bar
        fun drawSegment(color: Int, amount: Int) {
            val end = start + (amount * scalefactor).toInt()
            paint.color = color
            canvas.drawRect(start.toFloat(), bartop.toFloat(), end.toFloat(), barbottom.toFloat(), paint)

            start = end
        }

        drawSegment(SharpnessColors.RED, ired)
        drawSegment(SharpnessColors.ORANGE, iorange)
        drawSegment(SharpnessColors.YELLOW, iyellow)
        drawSegment(SharpnessColors.GREEN, igreen)
        drawSegment(SharpnessColors.BLUE, iblue)
        drawSegment(SharpnessColors.WHITE, iwhite)
        drawSegment(SharpnessColors.PURPLE, ipurple)
    }
}