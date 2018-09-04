package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.common.TreeNode
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.WeaponTree
import com.gatheringhallstudios.mhworlddatabase.data.types.TreeFormatter
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType

class WeaponTreeListViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).weaponDao()
    var weaponPaths: List<TreeNode<WeaponTree>> = listOf()
    private lateinit var currentWeaponType: WeaponType

    fun setWeaponType(weaponType: WeaponType) {
        if (::currentWeaponType.isInitialized && currentWeaponType == weaponType) {
            return
        }

        currentWeaponType = weaponType

        val weaponTreeData = dao.loadWeaponTrees(AppSettings.dataLocale, weaponType)
        val weaponTreeRoots = buildTree(weaponTreeData)
        val buffer: List<MutableList<TreeNode<WeaponTree>>> = weaponTreeRoots.flatMap { x ->
            findWeaponPaths(x, 0, listOf(), true)
        }

        weaponPaths = buffer.flatten().distinct()
    }

    private fun buildTree(weaponList: List<WeaponTree>): List<TreeNode<WeaponTree>> {
        //Build all nodes first
        val weapons = mutableMapOf<Int, TreeNode<WeaponTree>>()
        for (weaponTree in weaponList) {
            weapons[weaponTree.id] = TreeNode(weaponTree)
        }

        //Add child relationships
        for (weapon in weapons) {
            val parentId = weapon.value.value.previous_weapon_id
            if (parentId != null) {
                weapons[parentId]!!.addChild(weapon.value) //This search will always find something unless the data is somehow malformed
            }
        }

        //Return base nodes
        return weapons.filter { k -> k.value.parent == null }.values.toList()
    }

    /**
     * Performs a depth first traversal on the weapon tree to find all possible routes and creates the enum representing the tree component structure to be drawn
     */
    private fun findWeaponPaths(node: TreeNode<WeaponTree>, depth: Int, prefix: List<TreeFormatter>, isTail: Boolean): MutableList<MutableList<TreeNode<WeaponTree>>> {
        val paths: MutableList<MutableList<TreeNode<WeaponTree>>> = mutableListOf()

        node.value.depth = depth

        //The root node gets a special enum to show it is the start node
        if (depth == 0) {
            val formatter = prefix.toMutableList()
            formatter.add(TreeFormatter.START)
            node.value.formatter = formatter
        } else {
            val formatter = prefix.toMutableList()
            formatter.add(if (isTail) TreeFormatter.L_BRANCH else TreeFormatter.T_BRANCH)
            node.value.formatter = formatter
        }

        if (node.getChildren().isEmpty()) {
            node.value.formatter.add(TreeFormatter.END)
            val path: MutableList<TreeNode<WeaponTree>> = mutableListOf(node)
            paths.add(path)
        } else {
            //Root nodes are treated specially because of the way they are to be drawn on the UI
            //They do not receive mid nodes (even though they obviously have children)
            if (depth > 0) node.value.formatter.add(TreeFormatter.MID)

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
                val pathLists = findWeaponPaths(it, node.value.depth!! + 1, nextPrefix, nextIsTail)
                pathLists.forEach {
                    it.add(0, node)
                    paths.add(it)
                }
            }
        }

        return paths
    }
}

