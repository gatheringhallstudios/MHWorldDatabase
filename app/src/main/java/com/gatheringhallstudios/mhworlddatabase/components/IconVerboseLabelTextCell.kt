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
import com.gatheringhallstudios.mhworlddatabase.databinding.CellIconVerboseLabelTextBinding

class VerboseIconLabelTextCellBinder(val view: View) {
    private val binding = CellIconVerboseLabelTextBinding.bind(view)

    /**
     * Set custom drawable for the left icon
     */
    fun setIconDrawable(drawable: Drawable?) {
        binding.icon.setImageDrawable(drawable)

        // Invalidate to trigger layout update
        view.invalidate()
    }

    /**
     * Sets the icon decoration type
     */
    fun setIconType(type: IconType) {
        binding.icon.applyIconType(type)
    }

    fun setLabelText(titleText: String?) {
        binding.labelText.text = titleText
    }

    fun setSubLabelText(sublabelText: String?) {
        binding.sublabelText.isVisible = !sublabelText.isNullOrEmpty()
        binding.sublabelText.text = sublabelText
    }

    fun setValueText(value: String?) {
        binding.valueText.isVisible = !value.isNullOrEmpty()
        binding.valueText.text = value
    }

    fun setSubValueText(subValue: String?) {
        binding.subvalueText.isVisible = !subValue.isNullOrEmpty()
        binding.subvalueText.text = subValue
    }
}

class VerboseIconLabelTextCell : ConstraintLayout {
    lateinit var binder: VerboseIconLabelTextCellBinder
        private set

    constructor(context: Context) : super(context) {
        init(null, "", "", "", "")
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
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