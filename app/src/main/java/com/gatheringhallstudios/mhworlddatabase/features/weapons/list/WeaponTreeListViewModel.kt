package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.gatheringhallstudios.mhworlddatabase.common.TreeNode
import com.gatheringhallstudios.mhworlddatabase.data.models.WeaponBase
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType

class WeaponTreeListViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var weaponTreeData: TreeNode<WeaponBase>
    var weaponPaths: MutableList<MutableList<TreeNode<WeaponBase>>> = mutableListOf()
    val weaponTree: List<WeaponBase> = listOf()

    fun initializeTree() {
//         1
//        / \
//       2   3
//      / \
//     4   5

        //Create root
        weaponTreeData = TreeNode(WeaponBase(1, "TestWeapon1", WeaponType.CHARGE_BLADE, 15, 1000, 1, 1, 1))

        val testWeapon2 = TreeNode(WeaponBase(2, "TestWeapon2", WeaponType.CHARGE_BLADE, 20, 1500, 1, 1, 1))
        val testWeapon3 = TreeNode(WeaponBase(3, "TestWeapon3", WeaponType.CHARGE_BLADE, 20, 2000, 1, 1, 1))
        val testWeapon4 = TreeNode(WeaponBase(4, "TestWeapon4", WeaponType.CHARGE_BLADE, 20, 2500, 1, 1, 1))
        val testWeapon5 = TreeNode(WeaponBase(5, "TestWeapon5", WeaponType.CHARGE_BLADE, 20, 3000, 1, 1, 1))
        val testWeapon6 = TreeNode(WeaponBase(5, "TestWeapon6", WeaponType.CHARGE_BLADE, 20, 3000, 1, 1, 1))
        val testWeapon7 = TreeNode(WeaponBase(5, "TestWeapon7", WeaponType.CHARGE_BLADE, 20, 3000, 1, 1, 1))
        val testWeapon8 = TreeNode(WeaponBase(5, "TestWeapon8", WeaponType.CHARGE_BLADE, 20, 3000, 1, 1, 1))
        val testWeapon9 = TreeNode(WeaponBase(5, "TestWeapon9", WeaponType.CHARGE_BLADE, 20, 3000, 1, 1, 1))


        weaponTreeData.addChild(testWeapon2)
        weaponTreeData.addChild(testWeapon3)

        testWeapon2.addChild(testWeapon4)
        testWeapon2.addChild(testWeapon5)
        testWeapon5.addChild(testWeapon9)

        testWeapon3.addChild(testWeapon6)
        testWeapon3.addChild(testWeapon7)

        testWeapon6.addChild(testWeapon8)

        weaponPaths = findWeaponPaths(weaponTreeData, 0, "", true)
    }

    fun findWeaponPaths(node: TreeNode<WeaponBase>, depth: Int, prefix: String, isTail: Boolean): MutableList<MutableList<TreeNode<WeaponBase>>> {
        val paths: MutableList<MutableList<TreeNode<WeaponBase>>> = mutableListOf()

        node.value.depth = depth
        //Don't append a branch on the root node
        if (depth != 0) {
            node.value.formatting = prefix.plus(if (isTail) "└─" else "├─")
        }

        if (node.getChlidren().isEmpty()) {
            val path: MutableList<TreeNode<WeaponBase>> = mutableListOf(node)
            paths.add(path)
        } else {
            node.getChlidren().forEachIndexed {index, it ->
                val nextIsTail = index == node.getChlidren().size - 1
                val pathLists = findWeaponPaths(it, node.value.depth + 1, prefix.plus (if(isTail) "    " else "│   "), nextIsTail )
                pathLists.forEach {
                    it.add(0, node)
                    paths.add(it)
                }
            }
        }

        return paths
    }
}

