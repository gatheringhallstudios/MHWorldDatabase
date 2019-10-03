package com.gatheringhallstudios.mhworlddatabase.features.weapons

import com.gatheringhallstudios.mhworlddatabase.common.TreeNode
import com.gatheringhallstudios.mhworlddatabase.data.models.MHModelTree
import com.gatheringhallstudios.mhworlddatabase.data.models.MHParentedModel

/**
 * Represents the indents and the straight, L and T branches required to draw the tree for each row
 * of the wepaons tree
 */
enum class TreeFormatter {
    INDENT,

    /** A straight line with no connector. */

    STRAIGHT_BRANCH,
    L_BRANCH,
    T_BRANCH,

    START,

    /**
     * A special type of mid node used for entries with the same idepth level as the parent
     */
    THROUGH,

    /**
     * Added when this branch has children. This is visually replaced when the node is collapsed
     */
    MID,

    /**
     * End node used for entries at the same depth level
     */
    END,

    /**
     * End node used when this entry is indented.
     */
    END_INDENTED
}

/**
 * Represents end nodes in the tree that need to be colored
 */
enum class TreeNode {
    START,
    START_COLLAPSED,
    MID,
    MID_COLLAPSED,
    THROUGH,
    THROUGH_COLLAPSED,
    END,
    END_INDENTED
}

/**
 * Defines a tree node that has been rendered to display as a list item.
 */
class RenderedTreeNode<T>(
        val value: T,

        /**
         * The depth level of this node.
         * Note that this is the true depth, and not the rendered depth
         */
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
 * Creates a render list from a ModelTree object (which is actually a collection of multiple trees).
 */
fun <T: MHParentedModel> createTreeRenderList(trees: MHModelTree<T>)
        = trees.roots.flatMap { createTreeRenderList(it) }

/**
 * Performs a depth first traversal on the weapon tree node and returns a list of RenderedTreeNode objects representing all nodes.
 * to find all possible routes and creates the enum representing the tree component structure to be drawn
 */
fun <T> createTreeRenderList(node: TreeNode<T>, depth: Int = 0, prefix: List<TreeFormatter> = listOf(), isTail: Boolean = true): List<RenderedTreeNode<T>> {
    val isRoot = node.parent == null
    val isLeaf = node.getChildren().isEmpty()
    val isOnlyChild = node.parent?.getChildren()?.size == 1

    if (isRoot && isLeaf) {
        return listOf(RenderedTreeNode(node.value))
    }

    val paths: MutableList<RenderedTreeNode<T>> = mutableListOf()
    val formatter = mutableListOf<TreeFormatter>()

    formatter.addAll(prefix)

    //The root node gets a special enum to show it is the start node
    val newFormatter = when {
        isRoot -> TreeFormatter.START
        isOnlyChild -> when {
            isLeaf -> TreeFormatter.END
            else -> TreeFormatter.THROUGH
        }
        isTail -> TreeFormatter.L_BRANCH
        else -> TreeFormatter.T_BRANCH
    }

    formatter.add(newFormatter)

    // If the last formatter was an L or T branch, it needs to be followed up
    if (newFormatter == TreeFormatter.L_BRANCH || newFormatter == TreeFormatter.T_BRANCH) {
        if (isLeaf) {
            formatter.add(TreeFormatter.END_INDENTED)
        } else  {
            formatter.add(TreeFormatter.MID)
        }
    }

    val resultNode = RenderedTreeNode(node.value, depth, formatter, node.nestedChildrenCount)
    paths.add(resultNode)

    node.getChildren().forEachIndexed { index, it ->
        val nextPrefix = prefix.toMutableList()

        if (!isRoot && isTail && !isOnlyChild) {
            nextPrefix.add(TreeFormatter.INDENT)
        } else if (!isRoot && !isOnlyChild) {
            nextPrefix.add(TreeFormatter.STRAIGHT_BRANCH)
        }

        val nextIsTail = index == node.getChildren().size - 1
        val pathLists = createTreeRenderList(it, depth + 1, nextPrefix, nextIsTail)
        paths.addAll(pathLists)
    }

    return paths
}
