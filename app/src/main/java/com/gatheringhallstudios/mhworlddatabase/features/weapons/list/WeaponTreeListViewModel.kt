package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.common.MHModelTreeFilter
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.Weapon
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType
import com.gatheringhallstudios.mhworlddatabase.data.models.MHModelTree
import com.gatheringhallstudios.mhworlddatabase.features.weapons.RenderedTreeNode

/**
 * Viewmodel used to contain data for the Weapon Tree.
 */
class WeaponTreeListViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).weaponDao()
    private lateinit var currentWeaponType: WeaponType

    /**
     * A list of nodes for the tree to display.
     */
    val nodeListData = MutableLiveData<List<RenderedTreeNode<Weapon>>>()

    lateinit var weaponTree: MHModelTree<Weapon>
    private var filter: MHModelTreeFilter<Weapon>? = null

    fun setWeaponType(weaponType: WeaponType) {
        if (::currentWeaponType.isInitialized && currentWeaponType == weaponType) {
            return
        }

        currentWeaponType = weaponType
        weaponTree = dao.loadWeaponTrees(AppSettings.dataLocale, weaponType)
        filter = MHModelTreeFilter(weaponTree)

        updateNodeList()
    }

    /**
     * Sets whether the weapon node list should show only "final results".
     * If final is true, nodeListData will be replaced with leaf nodes.
     * Otherwise nodeListData will contain the entire tree.
     */
    fun setShowFinal(final: Boolean) {
        filter?.finalOnly = final
        updateNodeList()
    }

    private fun updateNodeList() {
        nodeListData.value = filter?.renderResults() ?: emptyList()
    }
}

