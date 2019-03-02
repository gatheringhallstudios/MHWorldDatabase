package com.gatheringhallstudios.mhworlddatabase.data.models

import com.gatheringhallstudios.mhworlddatabase.common.TreeNode

/**
 * A collection of weapon trees for a particular weapon type
 * todo: move to another file once we decided where it should go.
 */
class MHModelTree<T: MHParentedModel>(
        models: List<T>
) {
    /**
     * A list of all tree roots for this collection
     */
    val roots: List<TreeNode<T>>

    /**
     * A list of all "leaf nodes" in this weapon tree
     */
    val leaves: List<TreeNode<T>>

    private val modelMap: Map<Int, TreeNode<T>>

    init {
        // create a mapping of id -> weapon treenode
        modelMap = models.asSequence().map { TreeNode(it) }.associateBy { it.value.entityId }

        //Add child relationships
        for ((id, modelNode) in modelMap) {
            val parentId = modelNode.value.parentId
            if (parentId != null) {
                modelMap[parentId]?.addChild(modelNode)
            }
        }

        // Extract all root nodes and then all leaves
        roots = modelMap.filter { it.value.isRoot }.values.toList()

        val leaves = mutableListOf<TreeNode<T>>()
        for (root in roots) {
            if (root.isLeaf) {
                leaves.add(root)
            } else {
                leaves.addAll(root.leaves)
            }
        }

        this.leaves = leaves
    }

    /**
     * Method used to
     */
    fun getModel(weaponId: Int) = modelMap[weaponId]

    /**
     * Creates a list that crawls through every node in this list (depth first).
     * TODO: Make into an iterator once kotlin stops having issues with recursive sequences and iterators
     */
    fun flatten() = roots.flatMap { flattenNode(it) }

    /**
     * Helper that recursively creates a list including the node itself (depth search)
     */
    private fun flattenNode(node: TreeNode<T>): List<TreeNode<T>> {
        val result = mutableListOf<TreeNode<T>>()
        result.add(node)

        for (child in node.getChildren()) {
            result.addAll(flattenNode(child))
        }

        return result
    }
}