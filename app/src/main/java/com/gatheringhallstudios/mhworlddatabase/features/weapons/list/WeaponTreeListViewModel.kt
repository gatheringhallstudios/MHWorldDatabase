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
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponCategory
import com.gatheringhallstudios.mhworlddatabase.features.weapons.RenderedTreeNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Viewmodel used to contain data for the Weapon Tree.
 */
class WeaponTreeListViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).weaponDao()
    private lateinit var currentWeaponType: WeaponType

    /**
     * Encapsulates the charm tree and performs filtering on it
     */
    private val filter: MHModelTreeFilter<Weapon> = MHModelTreeFilter()

    /**
     * A list of nodes for the tree to display.
     */
    val nodeListData = MutableLiveData<List<RenderedTreeNode<Weapon>>>()

    /**
     * A list of nodes for the tree to display under the Kulve tab.
     */
    val kulveNodeData = MutableLiveData<List<RenderedTreeNode<Weapon>>>()

    /**
     * Returns the current filter state for final only
     */
    val isFinal get() = filter.finalOnly

    fun setWeaponType(weaponType: WeaponType) {
        if (::currentWeaponType.isInitialized && currentWeaponType == weaponType) {
            return
        }

        currentWeaponType = weaponType

        GlobalScope.launch(Dispatchers.Main) {
            val weaponTree = withContext(Dispatchers.IO) {
                dao.loadWeaponTrees(AppSettings.dataLocale, weaponType)
            }
            filter.tree = weaponTree
            updateNodeList()
        }
    }

    /**
     * Sets whether the weapon node list should show only "final results".
     * If final is true, nodeListData will be replaced with leaf nodes.
     * Otherwise nodeListData will contain the entire tree.
     */
    fun setShowFinal(final: Boolean) {
        filter.finalOnly = final
        updateNodeList()
    }

    private fun updateNodeList() {
        val results = filter.renderResults()
        nodeListData.value = results.filter { it.value.category == WeaponCategory.REGULAR }
        kulveNodeData.value = results.filter { it.value.category == WeaponCategory.KULVE }
    }
}

