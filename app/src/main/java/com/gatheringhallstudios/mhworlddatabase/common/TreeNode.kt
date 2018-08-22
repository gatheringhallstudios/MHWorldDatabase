package com.gatheringhallstudios.mhworlddatabase.common

import com.squareup.haha.guava.collect.ImmutableList

/**
 * This class is the tree node implementation for holding hierarchical data, e.g. weapons
 */
class TreeNode<T>(value: T) {
    private var parent: TreeNode<T>? = null

    val value: T = value
    private val children = mutableListOf<TreeNode<T>>()

    fun addChild(node: TreeNode<T>) {
        node.parent = this
        children.add(node)
    }

    fun getChlidren(): List<TreeNode<T>> {
        return children.toList()
    }
}