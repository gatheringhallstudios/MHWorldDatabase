package com.gatheringhallstudios.mhworlddatabase.util.tree

// note: could be made more efficient by enforcing immutable and caching results
// do so if this runs into performance issues (it likely won't, data set is too small)

/**
 * This class is the tree node implementation for holding hierarchical data, e.g. weapons
 */
class TreeNode<T>(val value: T) {
    var parent: TreeNode<T>? = null
        private set

    private val children = mutableListOf<TreeNode<T>>()

    /**
     * Adds a child to this tree.
     */
    fun addChild(node: TreeNode<T>) {
        node.parent = this
        children.add(node)
    }

    fun getChildren(): List<TreeNode<T>> {
        return children.toList()
    }

    /**
     * Recursive function that returns all leaf nodes.
     * TODO: could be made more efficient, maybe cached
     */
    val leaves: List<TreeNode<T>> get() = when {
        isLeaf -> emptyList()
        else -> {
            children.map {
                if (it.isLeaf) {
                    listOf(it)
                } else {
                    it.leaves
                }
            }.flatten()
        }
    }

    /**
     * Returns true if the node is a root node
     */
    val isRoot get() = parent == null

    /**
     * Returns true if this node is a leaf node
     */
    val isLeaf get() = children.isEmpty()

    /**
     * Returns the amount of direct children and sub-children
     * TODO: Make efficient. Perhaps addChild() should update the parent child total
     */
    val nestedChildrenCount get(): Int {
        return children.size + children.sumBy { it.nestedChildrenCount }
    }

    /**
     * Returns the path from the root to this node as a list as Tree Nodes
     */
    val path: List<TreeNode<T>> get() {
        val parent = this.parent
        if (parent == null) {
            return listOf(this)
        } else {
            return parent.path + listOf(this)
        }
    }

    /**
     * Creates a new TreeNodeType subtree linking up to the path,
     * returning the root node of that subtree.
     */
    fun getPathSubtree(): TreeNode<T> {
        val path = this.path
        val root = TreeNode(path[0].value)

        var currentNode = root
        for (child in path.drop(1)) {
            val newNode = TreeNode(child.value)
            currentNode.addChild(newNode)
            currentNode = newNode
        }

        return root
    }
}