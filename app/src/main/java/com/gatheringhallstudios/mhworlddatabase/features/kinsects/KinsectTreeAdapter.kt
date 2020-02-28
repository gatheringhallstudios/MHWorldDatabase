package com.gatheringhallstudios.mhworlddatabase.features.kinsects

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gatheringhallstudios.mhworlddatabase.adapters.EmptyState
import com.gatheringhallstudios.mhworlddatabase.adapters.EmptyStateAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.data.models.Kinsect
import com.gatheringhallstudios.mhworlddatabase.util.tree.RenderedTreeNode
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager

class KinsectTreeAdapter(onSelected: (Kinsect) -> Unit): androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private val delegatesManager = AdapterDelegatesManager<List<Any>>()

    init {
        delegatesManager.addDelegate(EmptyStateAdapterDelegate())
        delegatesManager.addDelegate(KinsectTreeListAdapterDelegate(
                onSelected=onSelected,
                onLongSelect=this::onToggle))
    }

    /**
     * Source items. When items are expanded, its pulled from this list.
     */
    private var sourceItems: List<RenderedTreeNode<Kinsect>> = emptyList()

    /**
     * Contains the sourceItems to display in the recyclerview itself.
     * When rendering, use resolvedItems instead of renderedItems, as resolvedItems includes the empty state.
     */
    private val renderedItems: MutableList<RenderedTreeNode<Kinsect>> = mutableListOf()

    /**
     * A mapping from kinsect id to the position of the rendered tree node representation
     */
    private val nodeMap = mutableMapOf<Int, RenderedTreeNode<Kinsect>>()

    /** Internal non-changing list of the empty state **/
    private val emptyState = listOf(EmptyState())

    /**
     * Returns the current items that need to be rendered in the recyclerview.
     * If the source items are empty, returns the empty state.
     */
    private val resolvedItems get() = when(sourceItems.isEmpty()) {
        true -> emptyState
        false -> renderedItems
    }

    /**
     * Binds the list of items to this adapter and notifies the update
     */
    fun setItems(items: List<RenderedTreeNode<Kinsect>>) {
        this.sourceItems = items
        nodeMap.clear()
        for (item in items) {
            nodeMap[item.value.id] = item
        }

        // add items to rendered, and handle the nodes that are collapsed.
        // If any node is collapsed, do not add subsequent ones until we "resurface" to the correct depth.
        renderedItems.clear()
        var depthLevel: Int? = null
        for (item in sourceItems) {
            // if this item is deeper than the collapse level, skip
            if (depthLevel != null && item.depth > depthLevel) {
                continue
            }

            // if we got here, not in collapsed data, clear (if needed) and add the item
            depthLevel = null
            renderedItems.add(item)

            // If collapsed, set the depth level and start ommiting items
            if (item.isCollapsed) {
                depthLevel = item.depth
            }
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        delegatesManager.onBindViewHolder(resolvedItems, position, holder, null)
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(resolvedItems, position)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return delegatesManager.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewDetachedFromWindow(holder)
    }

    /**
     * Returns the count of items currently visible
     */
    override fun getItemCount(): Int {
        return resolvedItems.size
    }

    /**
     * Internal function that toggles collapsed state.
     * Note: This has poor performance as its O(N) on both tables. This could probably be improved in some way
     * One idea is to construct using the actual tree and perform depth first search.
     * That way we map to the actual tree and can "reconstruct" on expand.
     */
    private fun onToggle(kinsect: Kinsect) {
        // todo: viewPosition might end up failing later... it depends on if "onBindViewHolder" causes an update for later entries

        val node = nodeMap[kinsect.id]
        if (node == null) {
            Log.e("KinsectTreeAdapter", "Tried to collapse/expand invalid kinsect")
            return
        }

        if (node.numChildren == 0) {
            return // cannot be toggled
        }

        val destinationPosition = renderedItems.indexOf(node)

        if (node.isCollapsed) {
            // we're expanding
            val sourcePosition = sourceItems.indexOf(node)
            val newItems = ArrayList<RenderedTreeNode<Kinsect>>()

            // Traverse source list and add all items while excluding collapsed items and their children
            var idx = sourcePosition + 1
            while (idx <= sourcePosition + node.numChildren) {
                newItems.add(sourceItems[idx])

                // Skip over collapsed children
                if (sourceItems[idx].isCollapsed) idx += sourceItems[idx].numChildren

                idx++
            }

            renderedItems.addAll(destinationPosition + 1, newItems)
            node.isCollapsed = false // no longer collapsed

            notifyItemChanged(destinationPosition)
            notifyItemRangeInserted(destinationPosition + 1, newItems.count())

        } else {
            // we're collapsing
            val startDepth = node.depth
            val startRemove = destinationPosition + 1

            // Get the last point to remove. Test nodes until we resurface to this node's depth level.
            var endRemove = startRemove
            for (idx in endRemove until renderedItems.size) {
                val testNode = renderedItems[idx]
                if (testNode.depth <= startDepth) {
                    break
                }
                endRemove = idx
            }

            // Remove items. Note that the end index is exclusive, necessitating + 1
            val itemsToRemove = renderedItems.subList(startRemove, endRemove + 1)
            val removalCount = itemsToRemove.size
            itemsToRemove.clear()

            // Update the node state and notify the recyclerview of removal
            node.isCollapsed = true // collapsed
            notifyItemChanged(destinationPosition)
            notifyItemRangeRemoved(destinationPosition + 1, removalCount)
        }
    }
}