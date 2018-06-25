package com.gatheringhallstudios.mhworlddatabase.adapters.common

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager

/**
 * Defines an adapter that can be used to add sectioned data.
 * SubHeaderAdapterDelegate and SectionHeaderAdapterDelegate are already added
 */
class CategoryAdapter(vararg delegates: AdapterDelegate<List<Any>>) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    private val delegatesManager = AdapterDelegatesManager<List<Any>>()

    private val items = ArrayList<Any>()

    init {
        delegatesManager.addDelegate(SectionHeaderAdapterDelegate())
        delegatesManager.addDelegate(SubHeaderAdapterDelegate())

        for (delegate in delegates) {
            delegatesManager.addDelegate(delegate)
        }
    }

    fun clear() {
        items.clear()
    }

    /**
     * Adds a group headed by a regular SectionHeader
     * @param name the title of the section header
     * @param items the items under that section header
     */
    fun addSection(name: String, items: Collection<Any>) {
        this.items.add(SectionHeader(name))
        this.items.addAll(items)
    }

    /**
     * Adds a group headed by a regular SubHeader
     * @param name the title of the sub header
     * @param items the items under that section header
     */
    fun addSubSection(name: String, items: Collection<Any>) {
        this.items.add(SubHeader(name))
        this.items.addAll(items)
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(items, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        return delegatesManager.onBindViewHolder(items, position, holder)
    }
}