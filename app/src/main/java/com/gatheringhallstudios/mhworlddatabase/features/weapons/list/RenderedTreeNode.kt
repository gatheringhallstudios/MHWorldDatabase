package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

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