package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.common.TreeFormatter
import com.gatheringhallstudios.mhworlddatabase.common.TreeNode
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.Weapon
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType

/**
 * A collection of weapon trees for a particular weapon type
 */
class WeaponTreeCollection(
        weapons: List<Weapon>
) {
    /**
     * A list of all tree roots for this collection
     */
    val roots: List<TreeNode<Weapon>>
    val weaponPaths: List<TreeNode<Weapon>>
    private val weaponMap: Map<Int, TreeNode<Weapon>>

    init {
        // create a mapping of id -> weapon treenode
        weaponMap = weapons.asSequence().map { TreeNode(it) }.associateBy { it.value.id }

        //Add child relationships
        for (weapon in weaponMap) {
            val parentId = weapon.value.value.previous_weapon_id
            if (parentId != null) {
                weaponMap[parentId]?.addChild(weapon.value)
            }
        }

        // Extract all root nodes
        roots = weaponMap.filter { k -> k.value.parent == null }.values.toList()

        // Populate depth/formatter fields (todo: separate concerns, but this works for now)
        val buffer: List<MutableList<TreeNode<Weapon>>> = roots.flatMap { x ->
            findWeaponPaths(x, 0, listOf(), true)
        }

        weaponPaths = buffer.flatten().distinct()
    }

    /**
     * Performs a depth first traversal on the weapon tree to find all possible routes and creates the enum representing the tree component structure to be drawn
     */
    private fun findWeaponPaths(node: TreeNode<Weapon>, depth: Int, prefix: List<TreeFormatter>, isTail: Boolean): MutableList<MutableList<TreeNode<Weapon>>> {
        val paths: MutableList<MutableList<TreeNode<Weapon>>> = mutableListOf()

        node.depth = depth

        //The root node gets a special enum to show it is the start node
        if (depth == 0) {
            val formatter = prefix.toMutableList()
            formatter.add(TreeFormatter.START)
            node.formatter = formatter
        } else {
            val formatter = prefix.toMutableList()
            formatter.add(if (isTail) TreeFormatter.L_BRANCH else TreeFormatter.T_BRANCH)
            node.formatter = formatter
        }

        if (node.getChildren().isEmpty()) {
            node.formatter.add(TreeFormatter.END)
            val path: MutableList<TreeNode<Weapon>> = mutableListOf(node)
            paths.add(path)
        } else {
            //Root nodes are treated specially because of the way they are to be drawn on the UI
            //They do not receive mid nodes (even though they obviously have children)
            if (depth > 0) node.formatter.add(TreeFormatter.MID)

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
                val pathLists = findWeaponPaths(it, node.depth!! + 1, nextPrefix, nextIsTail)
                pathLists.forEach {
                    it.add(0, node)
                    paths.add(it)
                }
            }
        }

        return paths
    }
}


class WeaponTreeListViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).weaponDao()
    private lateinit var currentWeaponType: WeaponType

    lateinit var weaponTree: WeaponTreeCollection

    fun setWeaponType(weaponType: WeaponType) {
        if (::currentWeaponType.isInitialized && currentWeaponType == weaponType) {
            return
        }

        currentWeaponType = weaponType

        val weaponTreeData = dao.loadWeaponTrees(AppSettings.dataLocale, weaponType)
        weaponTree = WeaponTreeCollection(weaponTreeData)
    }
}

