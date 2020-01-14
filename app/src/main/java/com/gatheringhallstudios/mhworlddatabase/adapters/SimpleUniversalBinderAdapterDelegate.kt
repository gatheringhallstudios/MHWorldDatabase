package com.gatheringhallstudios.mhworlddatabase.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleViewHolder
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.components.applyIconType
import kotlinx.android.synthetic.main.listitem_universal_simple.*

/**
 * An object that represents a binding for a simple result.
 * Simple bindings have a label, a value, an icon, and a click event handler.
 */
class SimpleUniversalBinding(
        val label: String?,
        val value: String?,
        val iconType: IconType,
        val icon: Drawable?,
        val subLabel: String?,
        val clickFn: (v: View) -> Unit
) {
    constructor(label: String?,
        value: String?,
        iconType: IconType,
        icon: Drawable?,
        clickFn: (v: View) -> Unit): this(label, value, iconType, icon, null, clickFn)
}

/**
 * Defines an interface used to create a binding given a context
 */
interface SimpleUniversalBinder {
    fun build(ctx: Context): SimpleUniversalBinding
}

/**
 * Helper function that creates a SimpleUniversalBinder using a lambda
 */
fun createSimpleUniversalBinder(fn: (Context) -> SimpleUniversalBinding): SimpleUniversalBinder {
    return object: SimpleUniversalBinder {
        override fun build(ctx: Context) = fn(ctx)
    }
}

/**
 * An adapter delegate that renders for any object with a label value and icon.
 * The views are homogenized, but require the use of special interim objects (called binders)
 * that return a data result (called bindings)
 */
class SimpleUniversalBinderAdapterDelegate: SimpleListDelegate<SimpleUniversalBinder>() {
    override fun isForViewType(obj: Any) = obj is SimpleUniversalBinder

    override fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.listitem_universal_simple, parent, false)
    }

    override fun bindView(viewHolder: SimpleViewHolder, data: SimpleUniversalBinder) {
        val result = data.build(viewHolder.context)

        with(viewHolder.icon) {
            applyIconType(result.iconType)
            setImageDrawable(result.icon)
        }

        viewHolder.label_text.text = result.label
        viewHolder.value_text.text = result.value
        viewHolder.sublabel_text.isVisible = !result.subLabel.isNullOrEmpty()
        viewHolder.sublabel_text.text = result.subLabel

        viewHolder.itemView.setOnClickListener { result.clickFn(viewHolder.itemView) }
    }

}