package com.gatheringhallstudios.mhworlddatabase.adapters.common

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlin.reflect.KClass

/**
 * A simple container-only viewholder used by SimpleListDelegate. Using a viewholder
 * when using KTX allows caching to work.
 */
class SimpleViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {
    val context get() = itemView.context
    val resources get() = itemView.resources
}

/**
 * The superclass for any AdapterDelegate with a uniform type that does not require a custom ViewHolder.
 * Subclasses of this type either use KTX for view binding (which caches findViewById lookups if given any viewholder),
 * or use a custom View implementation that doubles as a ViewHolder.
 *
 * Anything that requires more complicated logic should extend AdapterDelegate itself
 */
abstract class SimpleListDelegate<IClass : Any>: AdapterDelegate<List<Any>>() {

    /**
     * Returns the class that this delegate is for.
     */
    protected abstract fun isForViewType(obj: Any): Boolean

    /**
     * Constructs a new view object and returns it.
     * Similar to onCreateViewHolder, but returns only the view.
     */
    protected abstract fun onCreateView(parent: ViewGroup): View

    /**
     * Binds an instance of data to the view. This view may or may not be recycled.
     */
    protected abstract fun bindView(viewHolder: SimpleViewHolder, data: IClass)

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return isForViewType(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v = onCreateView(parent)
        return SimpleViewHolder(v)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder, payloads: MutableList<Any>) {
        val item = items[position] as IClass
        val viewHolder = holder as SimpleViewHolder
        bindView(viewHolder, item)
    }
}
