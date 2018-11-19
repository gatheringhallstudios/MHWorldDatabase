package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.common.TreeNode
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.Weapon
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType
import com.gatheringhallstudios.mhworlddatabase.data.models.WeaponTreeCollection



/**
 * Viewmodel used to contain data for the Weapon Tree.
 */
class WeaponTreeListViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).weaponDao()
    private lateinit var currentWeaponType: WeaponType
    private var finalOnly = false

    /**
     * A list of nodes for the tree to display.
     */
    val nodeListData = MutableLiveData<List<RenderedTreeNode<Weapon>>>()

    lateinit var weaponTree: WeaponTreeCollection

    fun setWeaponType(weaponType: WeaponType) {
        if (::currentWeaponType.isInitialized && currentWeaponType == weaponType) {
            return
        }

        currentWeaponType = weaponType
        weaponTree = dao.loadWeaponTrees(AppSettings.dataLocale, weaponType)

        updateNodeList()
    }

    /**
     * Sets whether the weapon node list should show only "final results".
     * If final is true, nodeListData will be replaced with leaf nodes.
     * Otherwise nodeListData will contain the entire tree.
     */
    fun setShowFinal(final: Boolean) {
        finalOnly = final
        updateNodeList()
    }

    private fun updateNodeList() {
        nodeListData.value = when {
            finalOnly -> {
                weaponTree.leaves.map {
                    RenderedTreeNode(it.value)
                }
            }
            else -> {
                // Populate depth/formatter fields (todo: separate concerns, but this works for now)
                // todo: consider moving to the WeaponTreeAdapter, and having it take the whole tree.
                val buffer = weaponTree.roots.flatMap { x ->
                    findWeaponPaths(x, 0, listOf(), true)
                }

                // Populate weapon paths and processed leaves
                buffer.flatten().distinct()
            }
        }
    }

    /**
     * Performs a depth first traversal on the weapon tree to find all possible routes and creates the enum representing the tree component structure to be drawn
     */
    private fun findWeaponPaths(node: TreeNode<Weapon>, depth: Int, prefix: List<TreeFormatter>, isTail: Boolean): MutableList<MutableList<RenderedTreeNode<Weapon>>> {
        val paths: MutableList<MutableList<RenderedTreeNode<Weapon>>> = mutableListOf()

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
            paths.add(mutableListOf(RenderedTreeNode(node.value, depth, formatter, node.nestedChildrenCount)))
            return paths
        }

        //Root nodes are treated specially because of the way they are to be drawn on the UI
        //They do not receive mid nodes (even though they obviously have children)
        if (depth > 0) formatter.add(TreeFormatter.MID)

        val resultNode = RenderedTreeNode(node.value, depth, formatter, node.nestedChildrenCount)
        paths.add(mutableListOf(resultNode))

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
            val pathLists = findWeaponPaths(it, depth + 1, nextPrefix, nextIsTail)
            pathLists.forEach {
                //it.add(0, node)
                paths.add(it)
            }
        }

        return paths
    }
}

