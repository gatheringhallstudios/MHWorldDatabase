package com.gatheringhallstudios.mhworlddatabase.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell

/**
 * An object that represents a binding for a simple result.
 * Simple bindings have a label, a value, an icon, and a click event handler.
 */
class SimpleUniversalBinding(
        val label: String?,
        val value: String?,
        val icon: Drawable?,
        val clickFn: (v: View) -> Unit
)

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
class SimpleUniversalBinderAdapterDelegate: SimpleListDelegate<SimpleUniversalBinder, IconLabelTextCell>() {
    override fun isForViewType(obj: Any) = obj is SimpleUniversalBinder

    override fun onCreateView(parent: ViewGroup): View {
        return IconLabelTextCell(parent.context)
    }

    override fun bindView(view: IconLabelTextCell, data: SimpleUniversalBinder) {
        val result = data.build(view.context)
        view.setLeftIconDrawable(result.icon)
        view.setLabelText(result.label)
        view.setValueText(result.value)

        view.setOnClickListener { result.clickFn(view) }
    }

}