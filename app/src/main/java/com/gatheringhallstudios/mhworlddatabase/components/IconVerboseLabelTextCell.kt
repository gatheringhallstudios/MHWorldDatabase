package com.gatheringhallstudios.mhworlddatabase.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.gatheringhallstudios.mhworlddatabase.R
import kotlinx.android.synthetic.main.cell_icon_verbose_label_text.view.*

class VerboseIconLabelTextCellBinder(val view: View) {
    /**
     * Set custom drawable for the left icon
     */
    fun setIconDrawable(drawable: Drawable?) {
        view.icon.setImageDrawable(drawable)

        // Invalidate to trigger layout update
        view.invalidate()
    }

    /**
     * Sets the icon decoration type
     */
    fun setIconType(type: IconType) {
        view.icon.applyIconType(type)
    }

    fun setLabelText(titleText: String?) {
        view.label_text.text = titleText
    }

    fun setSubLabelText(sublabelText: String?) {
        view.sublabel_text.isVisible = !sublabelText.isNullOrEmpty()
        view.sublabel_text.text = sublabelText
    }

    fun setValueText(value: String?) {
        view.value_text.isVisible = !value.isNullOrEmpty()
        view.value_text.text = value
    }

    fun setSubValueText(subValue: String?) {
        view.subvalue_text.isVisible = !subValue.isNullOrEmpty()
        view.subvalue_text.text = subValue
    }
}

class VerboseIconLabelTextCell : ConstraintLayout {
    lateinit var binder: VerboseIconLabelTextCellBinder
        private set

    constructor(context: Context?) : super(context) {
        init(null, "", "", "", "")
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        context ?: return

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.VerboseIconLabelTextCell)

        // Set values from attributes
        val drawable: Drawable?
        val labelText: String?
        val subLabelText: String?
        val valueText: String?
        val subValueText: String?
        try {
            drawable = attributes.getDrawable(R.styleable.VerboseIconLabelTextCell_iconSrc)
            labelText = attributes.getString(R.styleable.VerboseIconLabelTextCell_labelText)
            subLabelText = attributes.getString(R.styleable.VerboseIconLabelTextCell_subLabelText)
            valueText = attributes.getString(R.styleable.VerboseIconLabelTextCell_valueText)
            subValueText = attributes.getString(R.styleable.VerboseIconLabelTextCell_subValueText)
        } finally {
            // Typed arrays should be recycled after use
            attributes.recycle()
        }

        init(drawable, labelText, subLabelText, valueText, subValueText)
    }

    fun init(drawable: Drawable?, labelText: String?, subLabelText: String?, valueText: String?, subValueText: String?) {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.cell_icon_verbose_label_text, this, true)

        binder = VerboseIconLabelTextCellBinder(this)
        binder.setIconDrawable(drawable)
        binder.setLabelText(labelText)
        binder.setSubLabelText(subLabelText)
        binder.setValueText(valueText)
        binder.setSubValueText(subValueText)
    }
}