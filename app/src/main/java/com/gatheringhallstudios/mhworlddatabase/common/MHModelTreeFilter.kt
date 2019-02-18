package com.gatheringhallstudios.mhworlddatabase.common

import com.gatheringhallstudios.mhworlddatabase.data.models.MHModelTree
import com.gatheringhallstudios.mhworlddatabase.data.models.MHParentedModel
import com.gatheringhallstudios.mhworlddatabase.features.weapons.RenderedTreeNode
import com.gatheringhallstudios.mhworlddatabase.features.weapons.createTreeRenderList

/**
 * Filter object for any MHModelTree.
 * Filters can be applied and results can be requested
 */
class MHModelTreeFilter<T: MHParentedModel>(
        var tree: MHModelTree<T>
) {
    var finalOnly = false

    fun renderResults(): List<RenderedTreeNode<T>> {
        return when {
            finalOnly -> {
                tree.leaves.map {
                    RenderedTreeNode(it.value)
                }
            }
            else -> {
                // Render all root nodes and their children
                tree.roots.flatMap {
                    createTreeRenderList(it)
                }
            }
        }
    }
}
