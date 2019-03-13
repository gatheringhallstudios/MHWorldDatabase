package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.common.Filter
import com.gatheringhallstudios.mhworlddatabase.common.MHModelTreeFilter
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.Weapon
import com.gatheringhallstudios.mhworlddatabase.data.models.MHModelTree
import com.gatheringhallstudios.mhworlddatabase.data.types.*
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

    lateinit var currentWeaponType: WeaponType
        private set

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
     * Returns or updates the current filter state
     */
    var filterState = FilterState.default
        set(value) {
            field = value

            filter.clearFilters()

            filter.finalOnly = value.isFinalOnly
            filter.flatten = (value.sortBy != FilterSortCondition.NONE)
            // actual sorting is applied after the node list is updated

            if (value.elements.isNotEmpty()) {
                filter.addFilter(WeaponElementFilter(value.elements))
            }
            if (value.phials.isNotEmpty()) {
                filter.addFilter(WeaponPhialFilter(value.phials))
            }
            if (value.kinsectBonuses.isNotEmpty()) {
                filter.addFilter(WeaponKinsectFilter(value.kinsectBonuses))
            }
            if (value.shellingTypes.isNotEmpty()) {
                filter.addFilter(WeaponShellingFilter(value.shellingTypes))
            }
            if (value.shellingLevels.isNotEmpty()) {
                filter.addFilter(WeaponShellingLevelFilter(value.shellingLevels))
            }
            if (value.coatingTypes.isNotEmpty()) {
                filter.addFilter(WeaponCoatingFilter(value.coatingTypes))
            }
            if (value.specialAmmo != null) {
                filter.addFilter(WeaponSpecialAmmoFilter(value.specialAmmo))
            }

            updateNodeList()
        }

    /**
     * Sets the weapon type, and triggers the initial load.
     */
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
     * Triggers an update to perform the filter, do any post-filter mutations (sorting),
     * and update the LiveData with the results.
     */
    private fun updateNodeList() {
        val results = filter.renderResults().toMutableList()

        // Perform any mutations that are "post-filter". In this case, sorting.
        when (filterState.sortBy) {
            FilterSortCondition.ATTACK -> results.sortByDescending { it.value.attack_true }
            FilterSortCondition.AFFINITY -> results.sortByDescending { it.value.affinity }
            FilterSortCondition.NONE -> {}
        }

        nodeListData.value = results.filter { it.value.category == WeaponCategory.REGULAR }
        kulveNodeData.value = results.filter { it.value.category == WeaponCategory.KULVE }
    }
}

