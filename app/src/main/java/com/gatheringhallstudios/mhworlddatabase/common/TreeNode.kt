package com.gatheringhallstudios.mhworlddatabase.common

/**
 * This class is the tree node implementation for holding hierarchical data, e.g. weapons
 */
class TreeNode<T>(val value: T) {
    var parent: TreeNode<T>? = null

    private val children = mutableListOf<TreeNode<T>>()

    fun addChild(node: TreeNode<T>) {
        node.parent = this
        children.add(node)
    }

    fun getChildren(): List<TreeNode<T>> {
        return children.toList()
    }
}