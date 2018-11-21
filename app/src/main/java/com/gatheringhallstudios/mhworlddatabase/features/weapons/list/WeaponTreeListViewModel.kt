package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.Weapon
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType
import com.gatheringhallstudios.mhworlddatabase.data.models.WeaponTreeCollection
import com.gatheringhallstudios.mhworlddatabase.features.weapons.RenderedTreeNode
import com.gatheringhallstudios.mhworlddatabase.features.weapons.createTreeRenderList

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
                // Render all root nodes and their children
                weaponTree.roots.flatMap {
                    createTreeRenderList(it)
                }
            }
        }
    }
}

