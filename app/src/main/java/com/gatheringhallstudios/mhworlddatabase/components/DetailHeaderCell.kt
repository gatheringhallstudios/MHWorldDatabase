package com.gatheringhallstudios.mhworlddatabase.components

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.databinding.CellDetailHeaderBinding

/**
 * A reusable component for detail page headers.
 */
class DetailHeaderCell : ConstraintLayout {

    private val TAG = javaClass.simpleName

    private lateinit var binding: CellDetailHeaderBinding

    constructor(context: Context, @DrawableRes imgSrc: Int, titleText: String, descriptionText: String, subtitleText: String? = null) : super(context) {
        val drawable = AppCompatResources.getDrawable(getContext(), imgSrc)
        init(drawable, titleText, descriptionText, subtitleText)
    }

    constructor(context: Context) : super(context) {
        init(null, "", "", null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.DetailHeaderCell)

        // Set values from attributes
        val drawable: Drawable?
        val titleText: String?
        val descriptionText: String?
        val subtitleText: String?
        try {
            drawable = attributes.getDrawable(R.styleable.DetailHeaderCell_iconSrc)
            titleText = attributes.getString(R.styleable.DetailHeaderCell_titleText)
            descriptionText = attributes.getString(R.styleable.DetailHeaderCell_descriptionText)
            subtitleText = attributes.getString(R.styleable.DetailHeaderCell_subtitleText)
        } finally {
            // Typed arrays should be recycled after use
            attributes.recycle()
        }

        init(drawable, titleText, descriptionText, subtitleText)
    }

    fun init(drawable: Drawable?, titleText: String?, descriptionText: String?, subtitleText: String?) {
        val inflater = LayoutInflater.from(context)
        binding = CellDetailHeaderBinding.inflate(inflater, this)

        setTag(R.id.view_is_header, true)

        binding.headerIcon.setImageDrawable(drawable)
        binding.headerTitle.text = titleText
        binding.headerDescription.text = descriptionText

        if (!descriptionText.isNullOrEmpty()) {
            binding.headerDescription.visibility = View.VISIBLE
            binding.headerDescription.text = descriptionText
        }

        if (!subtitleText.isNullOrEmpty()) {
            binding.headerSubtitle.visibility = View.VISIBLE
            binding.headerSubtitle.text = subtitleText
        }

        layoutParams = ViewGroup.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    /**
     * Set custom drawable for the left icon
     */
    fun setIconDrawable(drawable: Drawable?) {
        binding.headerIcon.setImageDrawable(drawable)

        // Invalidate to trigger layout update
        invalidate()
    }

    /**
     * Sets the icon decoration type
     */
    fun setIconType(type: IconType) {
        binding.headerIcon.applyIconType(type)
    }

    fun setTitleText(titleText: String?) {
        binding.headerTitle.text = titleText
    }

    fun setDescriptionText(descriptionText: String?) {
        binding.headerDescription.visibility = when (descriptionText.isNullOrEmpty()) {
            true -> View.GONE
            false -> View.VISIBLE
        }
        binding.headerDescription.text = descriptionText
    }

    fun setSubtitleText(subtitleText: String?) {
        binding.headerSubtitle.visibility = View.VISIBLE
        binding.headerSubtitle.text = subtitleText
    }

    fun setSubtitleColor(color: Int) {
        binding.headerSubtitle.setTextColor(color)
    }

    fun updateDescriptionVisibility() {

    }

    /**
     * Removes the decorator around the icon.
     * TODO Make this an instantiation flag if necessary
     */
    fun removeDecorator() {
        binding.headerIcon.background = null
        binding.headerIcon.setPadding(0, 0, 0, 0)
    }
}
