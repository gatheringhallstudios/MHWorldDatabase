package com.gatheringhallstudios.mhworlddatabase.adapters.common

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager

/**
 * Defines an interface for a "section" of the CategoryAdapter.
 * This might eventually be extended to support collapsing.
 * Note: AdapterSection live updating is untested. Beware of potential errors.
 */
interface AdapterSection {
    val size: Int
    fun update(items: List<Any>)
}

/**
 * Defines an adapter that can be used to add sectioned data.
 * SubHeaderAdapterDelegate and SectionHeaderAdapterDelegate are already added.
 * Note: AdapterSection live updating is untested. Beware of potential errors.
 */
class CategoryAdapter(vararg delegates: AdapterDelegate<List<Any>>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val delegatesManager = AdapterDelegatesManager<List<Any>>()

    private val sections = ArrayList<Section>()
    private val items = ArrayList<Any>()

    init {
        delegatesManager.addDelegate(SectionHeaderAdapterDelegate())
        delegatesManager.addDelegate(SubHeaderAdapterDelegate())

        for (delegate in delegates) {
            delegatesManager.addDelegate(delegate)
        }
    }

    fun clear() {
        val previousSize = items.size
        items.clear()
        notifyItemRangeRemoved(0, previousSize)
    }

    /**
     * Internal method. Handles adding the section to the list
     */
    private fun addSectionInner(section: Section) {
        this.sections.add(section)
        section.addSelfTo(items)
        notifyItemRangeInserted(items.size - section.size, section.size)
    }

    /**
     * Adds a group headed by a regular SectionHeader
     * @param name the title of the section header
     * @param items the items under that section header
     */
    fun addSection(name: String, items: Collection<Any>): AdapterSection {
        val section = Section(SectionHeader(name), items)
        addSectionInner(section)
        return section
    }

    /**
     * Adds a group headed by a regular SectionHeader,
     * with sub-groups headed by regular subheaders
     */
    fun addSection(name: String, subSections: Map<String, Collection<Any>>): AdapterSection {
        val itemsToAdd = mutableListOf<Any>()
        for ((key, value) in subSections) {
            itemsToAdd.add(SubHeader(key))
            itemsToAdd.addAll(value)
        }
        return this.addSection(name, itemsToAdd)
    }

    /**
     * Adds a group headed by a regular SubHeader
     * @param name the title of the sub header
     * @param items the items under that section header
     */
    fun addSubSection(name: String, items: Collection<Any>): AdapterSection {
        val section = Section(SubHeader(name), items)
        addSectionInner(section)
        return section
    }

    fun addSections(sections: Map<String, Collection<Any>>, skipEmpty: Boolean = false) {
        val itemsToAdd = mutableListOf<Any>()
        for ((key, value) in sections) {
            if (skipEmpty && value.isEmpty()) {
                continue
            }
            itemsToAdd.add(SectionHeader(key))
            itemsToAdd.add(SubHeader(key))
            addSection(key, value)
        }
    }

    fun addSubSections(sections: Map<String, Collection<Any>>, skipEmpty: Boolean = false) {
        for ((key, value) in sections) {
            if (skipEmpty && value.isEmpty()) {
                continue
            }
            addSubSection(key, value)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(items, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return delegatesManager.onBindViewHolder(items, position, holder)
    }

    inner class Section(val header: Any, var items: Collection<Any> = emptyList()): AdapterSection {
        override val size get() = 1 + items.size

        override fun update(items: List<Any>) {
            // references to parent objects
            val parentSections = this@CategoryAdapter.sections
            val parentItems = this@CategoryAdapter.items

            val sectionIndex = parentSections.indexOf(this)

            // figure out how much of the list doesn't have to change
            // delete everything after (we'll add it back soon)
            val previousSections = parentSections.subList(0, sectionIndex)
            val firstIdx = previousSections.sumBy { it.size }
            parentItems.subList(firstIdx, parentItems.size).clear()

            // store a reference to the previous size
            val previousSize = this.items.size
            this.items = items

            // add everything back
            for (section in parentSections.subList(sectionIndex, parentSections.size)) {
                section.addSelfTo(parentItems)
            }

            // send notifications
            notifyItemRangeRemoved(firstIdx, previousSize)
            notifyItemRangeInserted(firstIdx, items.size)
        }

        fun addSelfTo(aggregate: MutableList<Any>) {
            aggregate.add(header)
            aggregate.addAll(items)
        }
    }
}