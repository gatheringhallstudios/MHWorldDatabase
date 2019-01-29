package com.gatheringhallstudios.mhworlddatabase.adapters.common

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Defines an adapter for a simple item meant to be used via KTX.
 * For an adapter with multiple items, use a delegate instead.
 */
abstract class SimpleRecyclerViewAdapter<T>: androidx.recyclerview.widget.RecyclerView.Adapter<SimpleViewHolder>() {
    protected abstract fun onCreateView(parent: ViewGroup): View
    protected abstract fun bindView(viewHolder: SimpleViewHolder, data: T)

    // internal modifiable list
    private val itemSource = mutableListOf<T>()

    var items: List<T> = emptyList()
        get() = Collections.unmodifiableList(field)

        /**
         * Updates the items in this adapter and calls notifyDataSetChanged
         */
        set(newItems) {
            field = newItems
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val v = onCreateView(parent)
        return SimpleViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        val item = items[position]
        bindView(holder, item)
    }
}