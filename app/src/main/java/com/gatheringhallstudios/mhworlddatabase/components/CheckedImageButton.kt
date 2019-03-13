package com.gatheringhallstudios.mhworlddatabase.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.view.marginEnd
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.util.dpToPx

/**
 * A custom ImageButton object that is checkable.
 * This ImageButton can also have text applied right next to the image.
 */
class CheckedImageButton  @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
): CheckedLinearLayout(context, attrs, defStyleAttr) {
    private val imageView = ImageView(context)
    private val textView = TextView(context)

    private val imageViewParams = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
        marginStart = 0
        gravity = Gravity.CENTER_VERTICAL
    }

    var text: CharSequence?
        /** Returns the text of the button */
        get() = textView.text
        /** Sets the button text, and changes visibility of the text depending on value */
        set(value) {
            textView.text = value
            textView.isVisible = !value.isNullOrEmpty()
            handleDataChange()
        }

    init {
        imageView.isVisible = false
        textView.isVisible = false

        imageView.layoutParams = imageViewParams

        textView.layoutParams = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER_VERTICAL
        }

        orientation = HORIZONTAL
        addView(imageView)
        addView(textView)

        if (attrs != null) {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.CheckedImageButton)
            try {
                val drawable = attributes.getDrawable(R.styleable.CheckedImageButton_src)
                val text = attributes.getString(R.styleable.CheckedImageButton_text)
                val checked = attributes.getBoolean(R.styleable.CheckedImageButton_checked, false)
                var imageWidth =  attributes.getDimension(R.styleable.CheckedImageButton_imageWidth, -1f)
                var imageHeight =  attributes.getDimension(R.styleable.CheckedImageButton_imageHeight, -1f)
                val imageSize =  attributes.getDimension(R.styleable.CheckedImageButton_imageSize, -1f)

                if (imageSize > 0) {
                    imageWidth = imageSize
                    imageHeight = imageSize
                }
                if (imageWidth > 0) {
                    imageView.layoutParams.width = imageWidth.toInt()
                }
                if (imageHeight > 0) {
                    imageView.layoutParams.height = imageHeight.toInt()
                }

                setImageDrawable(drawable)
                this.text = text
                this.isChecked = checked
            } finally {
                attributes.recycle()
            }
        }
    }

    /**
     * Sets the internal image drawable, and updates the image visiblity to reflect it.
     */
    fun setImageDrawable(image: Drawable?) {
        imageView.setImageDrawable(image)
        imageView.isVisible = (image != null)
        handleDataChange()
    }

    private fun handleDataChange() {
        if (imageView.isVisible && textView.isVisible) {
            imageViewParams.marginEnd = dpToPx(4) // default value (todo: make configurable)
        } else {
            imageViewParams.marginEnd = 0
        }
    }
}