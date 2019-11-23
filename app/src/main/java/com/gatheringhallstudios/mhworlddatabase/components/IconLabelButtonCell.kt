package com.gatheringhallstudios.mhworlddatabase.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.gatheringhallstudios.mhworlddatabase.R
import kotlinx.android.synthetic.main.cell_icon_label_button.view.*

/**
 * This is a full height, full width cell that displays an icon, label, and value. Used to generate
 * data rows in RecyclerView or inside XML layouts.
 */

class IconLabelButtonCell : ConstraintLayout {

    private val TAG = javaClass.simpleName

    constructor(context: Context, @DrawableRes imgSrc: Int, labelText: String) : super(context) {
        val drawable = AppCompatResources.getDrawable(getContext(), imgSrc)
        init(drawable, labelText)
    }

    constructor(context: Context) : super(context) {
        init(null, "")
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.IconLabelButtonCell)

        // Set values from attributes
        val drawable: Drawable?
        val labelText: String?
        try {
            drawable = attributes.getDrawable(R.styleable.IconLabelButtonCell_iconSrc)
            labelText = attributes.getString(R.styleable.IconLabelButtonCell_labelText2)
        } finally {
            // Typed arrays should be recycled after use
            attributes.recycle()
        }

        init(drawable, labelText)
    }

    fun init(drawable: Drawable?, labelText: String?) {
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.cell_icon_label_button, this, true)

        ButterKnife.bind(this)

        setLeftIconDrawable(drawable)
        setLabelText(labelText)


        layoutParams = ViewGroup.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        // makes this cell have touch feedback...since the xml is a merge tag, we can't do this in xml
        val highlightBackground = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, highlightBackground, true)
        setBackgroundResource(highlightBackground.resourceId)
    }

    /**
     * Set custom drawable for the left icon
     */
    fun setLeftIconDrawable(drawable: Drawable?) {
        generic_icon.setImageDrawable(drawable)

        // Invalidate to trigger layout update
        invalidate()
    }

    fun setLeftIconType(type: IconType) {
        generic_icon.applyIconType(type)
    }

    fun setLabelText(labelText: String?) {
        label_text.text = labelText
    }

    fun setButtonClickFunction(clickFunction: ()-> Unit) {
        cell_button.setOnClickListener {
            clickFunction()
        }
    }

    /**
     * Removes the decorator around the icon.
     * TODO Make this an instantiation flag if necessary
     */
    fun removeDecorator() {
        generic_icon!!.background = null
        generic_icon!!.setPadding(0, 0, 0, 0)
    }
}
