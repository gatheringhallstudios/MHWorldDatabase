package com.gatheringhallstudios.mhworlddatabase.adapters.common

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlin.reflect.KClass

/**
 * The superclass for any AdapterDelegate with a uniform type that does not require a ViewHolder.
 * Subclasses of this type either use KTX for view binding (which caches findViewById lookups),
 * or use a custom View implementation that doubles as a ViewHolder.
 *
 * Anything that requires more complicated logic should extend AdapterDelegate itself
 */
abstract class SimpleListDelegate<T : Any, V: View>: AdapterDelegate<List<Any>>() {

    /**
     * Returns the class that this delegate is for.
     */
    protected abstract fun getDataClass() : KClass<T>

    /**
     * Constructs a new view object and returns it.
     * Similar to onCreateViewHolder, but returns only the view.
     */
    protected abstract fun onCreateView(parent: ViewGroup) : View

    /**
     * Binds an instance of data to the view. This view may or may not be recycled.
     */
    protected abstract fun bindView(view: V, data: T)

    // Subclasses don't require a viewholder, but superclasses do
    class UselessViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView)

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position]::class == getDataClass()
    }

    override fun onCreateViewHolder(parent: ViewGroup): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val v = onCreateView(parent)
        return UselessViewHolder(v)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(items: List<Any>, position: Int, holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, payloads: MutableList<Any>) {
        val item = items[position] as T
        val view = holder.itemView as V
        bindView(view, item)
    }
}
