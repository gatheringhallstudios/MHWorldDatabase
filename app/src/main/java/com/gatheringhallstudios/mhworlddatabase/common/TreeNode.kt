package com.gatheringhallstudios.mhworlddatabase.common

/**
 * Represents the indents and the straight, L and T branches required to draw the tree for each row
 * of the wepaons tree
 */
enum class TreeFormatter {
    INDENT,
    STRAIGHT_BRANCH,
    L_BRANCH,
    T_BRANCH,
    START,
    MID,
    END
}

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

    // todo: the below are probably temporary. These should probably not be on a node but on a depth first search render result
    var depth: Int? = 0
    var formatter: MutableList<TreeFormatter> = mutableListOf()
}