package com.gatheringhallstudios.mhworlddatabase.features.kinsects.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.Kinsect
import com.gatheringhallstudios.mhworlddatabase.util.tree.MHModelTreeFilter
import com.gatheringhallstudios.mhworlddatabase.util.tree.RenderedTreeNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Viewmodel used to contain data for the Kinsect Tree.
 */
class KinsectTreeViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).kinsectDao()

    /**
     * Encapsulates the kinsect tree and performs filtering on it
     */
    private val filter: MHModelTreeFilter<Kinsect> = MHModelTreeFilter()

    /**
     * A list of nodes for the tree to display.
     */
    val nodeListData = MutableLiveData<List<RenderedTreeNode<Kinsect>>>()

    /**
     * Live event that reflects the current is filtered status
     */
    val isFilteredData = MutableLiveData<Boolean>()

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

            if (value.attackTypes.isNotEmpty()) {
                filter.addFilter(KinsectAttackTypeFilter(value.attackTypes))
            }
            if (value.dustEffects.isNotEmpty()) {
                filter.addFilter(KinsectDustEffectFilter(value.dustEffects))
            }

            updateNodeList()
            isFilteredData.value = filter.isFiltered
        }

    init {
        GlobalScope.launch(Dispatchers.Main) {
            val kinsectTree = withContext(Dispatchers.IO) {
                dao.loadKinsectTrees(AppSettings.dataLocale)
            }

            filter.tree = kinsectTree
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
            FilterSortCondition.POWER -> results.sortByDescending { it.value.power }
            FilterSortCondition.SPEED -> results.sortByDescending { it.value.speed }
            FilterSortCondition.HEAL -> results.sortByDescending { it.value.heal }
            FilterSortCondition.NONE -> {}
        }

        nodeListData.value = results
    }
}

