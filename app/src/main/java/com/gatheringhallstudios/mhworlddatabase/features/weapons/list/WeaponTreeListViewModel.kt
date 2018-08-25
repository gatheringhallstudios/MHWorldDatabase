package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.common.TreeNode
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.WeaponTree
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType

class WeaponTreeListViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).weaponDao()

    lateinit var weaponTreeRoots: List<TreeNode<WeaponTree>>
    var weaponPaths: MutableList<MutableList<TreeNode<WeaponTree>>> = mutableListOf()
    private lateinit var currentWeaponType : WeaponType

    fun setWeaponType(weaponType: WeaponType) {

        if (!::currentWeaponType.isInitialized || currentWeaponType != weaponType) {
            currentWeaponType = weaponType
        }
        else {
            return
        }

        val weaponTreeData = dao.loadWeaponTrees(AppSettings.dataLocale, weaponType.type)
        buildTree(weaponTreeData)
        weaponTreeRoots.forEach {x ->
            weaponPaths.addAll(findWeaponPaths(x, 0, "", true))
        }
    }

    private fun buildTree(weaponList: List<WeaponTree>) {
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
        weaponTreeRoots =  weapons.filter {k -> k.value.parent == null}.values.toList()
    }

    private fun findWeaponPaths(node: TreeNode<WeaponTree>, depth: Int, prefix: String, isTail: Boolean): MutableList<MutableList<TreeNode<WeaponTree>>> {
        val paths: MutableList<MutableList<TreeNode<WeaponTree>>> = mutableListOf()

        node.value.depth = depth
        //Don't append a branch on the root node
        if (depth != 0) {
            node.value.formatting = prefix.plus(if (isTail) "└" else "├")
        }

        if (node.getChlidren().isEmpty()) {
            val path: MutableList<TreeNode<WeaponTree>> = mutableListOf(node)
            paths.add(path)
        } else {
            node.getChlidren().forEachIndexed { index, it ->
                val nextIsTail = index == node.getChlidren().size - 1
                val pathLists = findWeaponPaths(it, node.value.depth!! + 1, prefix.plus(if (isTail) if(depth != 0 ) "  " else "" else "│"), nextIsTail)
                pathLists.forEach {
                    it.add(0, node)
                    paths.add(it)
                }
            }
        }

        return paths
    }
}

