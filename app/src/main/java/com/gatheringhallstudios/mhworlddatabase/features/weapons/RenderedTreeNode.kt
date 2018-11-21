package com.gatheringhallstudios.mhworlddatabase.features.weapons

import com.gatheringhallstudios.mhworlddatabase.common.TreeNode

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

    /**
     * Added when this branch has children. This is visually replaced when the node is collapsed
     */
    MID,

    END
}

/**
 * Defines a tree node that has been rendered to display as a list item.
 */
class RenderedTreeNode<T>(
        val value: T,
        val depth: Int,
        val formatter: List<TreeFormatter>,
        val numChildren: Int,
        var isCollapsed: Boolean = false
) {
    /**
     * Alternative constructor used for "non-tree" usage.
     */
    constructor(value: T): this(value, 0, listOf(TreeFormatter.INDENT), 0)
}


/**
 * Performs a depth first traversal on the weapon tree node and returns a list of RenderedTreeNode objects representing all nodes.
 * to find all possible routes and creates the enum representing the tree component structure to be drawn
 */
fun <T> createTreeRenderList(node: TreeNode<T>, depth: Int = 0, prefix: List<TreeFormatter> = listOf(), isTail: Boolean = true): List<RenderedTreeNode<T>> {
    if (depth == 0 && node.getChildren().isEmpty()) {
        return listOf(RenderedTreeNode(node.value))
    }

    val paths: MutableList<RenderedTreeNode<T>> = mutableListOf()
    val formatter = mutableListOf<TreeFormatter>()

    //The root node gets a special enum to show it is the start node
    val newFormatter = when {
        depth == 0 -> TreeFormatter.START
        isTail -> TreeFormatter.L_BRANCH
        else -> TreeFormatter.T_BRANCH
    }

    formatter.addAll(prefix)
    formatter.add(newFormatter)

    if (node.getChildren().isEmpty()) {
        paths.add(RenderedTreeNode(node.value, depth, formatter, node.nestedChildrenCount))
        return paths
    }

    //Root nodes are treated specially because of the way they are to be drawn on the UI
    //They do not receive mid nodes (even though they obviously have children)
    if (depth > 0) formatter.add(TreeFormatter.MID)

    val resultNode = RenderedTreeNode(node.value, depth, formatter, node.nestedChildrenCount)
    paths.add(resultNode)

    node.getChildren().forEachIndexed { index, it ->
        val nextPrefix = prefix.toMutableList()

        if (isTail) {
            if (depth != 0) { //Only add the indent if this is not a root tree element
                nextPrefix.add(TreeFormatter.INDENT)
            }
        } else {
            nextPrefix.add(TreeFormatter.STRAIGHT_BRANCH)
        }

        val nextIsTail = index == node.getChildren().size - 1
        val pathLists = createTreeRenderList(it, depth + 1, nextPrefix, nextIsTail)
        paths.addAll(pathLists)
    }

    return paths
}