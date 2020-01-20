package com.gatheringhallstudios.mhworlddatabase.util.tree

import com.gatheringhallstudios.mhworlddatabase.data.models.MHModelTree
import com.gatheringhallstudios.mhworlddatabase.data.models.MHParentedModel

/**
 * Interface that defines a basic sub filter to the main filter object
 */
interface Filter<T> {
    fun runFilter(obj: T): Boolean
}

/**
 * Filter object for any MHModelTree.
 * Filters can be applied and results can be requested
 */
class MHModelTreeFilter<T: MHParentedModel>(
        var tree: MHModelTree<T>? = null
) {
    private val filterList = mutableListOf<Filter<T>>()

    /**
     * If true, flattens the tree before sorting. Otherwise maintains tree structure
     */
    var flatten = false

    /**
     * Enables or disables whether or not to show only final entries.
     * More efficient than creating a custom final filter.
     * Setting this to true override flatten.
     */
    var finalOnly = false

    /**
     * Returns true if the filter is in a non-passthrough state.
     */
    val isFiltered get() = flatten || finalOnly || filterList.isNotEmpty()

    /**
     * Clears registered filters.
     */
    fun clearFilters() {
        filterList.clear()
    }

    /**
     * Adds a filter to the filter list.
     * The filter is applied during renderResults()
     */
    fun addFilter(filter: Filter<T>) {
        filterList.add(filter)
    }

    private fun testNode(node: TreeNode<T>): Boolean {
        return filterList.all { it.runFilter(node.value) }
    }

    /**
     * Runs through the tree, returning a collection of filter results.
     */
    fun renderResults(): List<RenderedTreeNode<T>> {
        val tree = this.tree ?: return emptyList()

        val needsFlatten = flatten || finalOnly

        // Return all root nodes as is if not filtering
        if (!isFiltered) {
            return createTreeRenderList(tree)
        }

        if (needsFlatten) {
            val nodes = when (finalOnly) {
                true -> tree.leaves
                false -> tree.flatten()
            }

            return nodes.asSequence()
                    .filter { testNode(it) }
                    .map { RenderedTreeNode(it.value) }
                    .toList()
        } else {
            // We're not flattening, so test items and reparent them in a new tree
            val successfulItems = mutableListOf<T>()
            for (node in tree.flatten()) {
                if (testNode(node)) {
                    successfulItems.add(node.value)
                }
            }

            val newTree = MHModelTree(successfulItems)
            return createTreeRenderList(newTree)
        }
    }
}
