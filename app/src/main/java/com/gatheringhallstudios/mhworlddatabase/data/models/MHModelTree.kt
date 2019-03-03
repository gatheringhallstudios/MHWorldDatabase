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
}