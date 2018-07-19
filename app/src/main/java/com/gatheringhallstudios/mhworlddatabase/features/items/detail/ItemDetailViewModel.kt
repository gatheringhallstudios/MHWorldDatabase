package com.gatheringhallstudios.mhworlddatabase.features.items.detail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.gatheringhallstudios.mhworlddatabase.AppSettings

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemCombination
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemLocation
import com.gatheringhallstudios.mhworlddatabase.data.models.Item
import com.gatheringhallstudios.mhworlddatabase.mergeLiveData

data class UsageData(
        val craftRecipes: List<ItemCombination>
)

data class AcquisitionData(
        val craftRecipes: List<ItemCombination>,
        val locations: List<ItemLocation>
)

/**
 * A viewmodel that contains information for a single item.
 * This viewmodel should be attached to the pager fragment,
 * and accessed through the parent for sub-fragments
 */
class ItemDetailViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = MHWDatabase.getDatabase(app).itemDao()

    var itemId = -1
        private set

    lateinit var item: LiveData<Item>

    lateinit var usageData: LiveData<UsageData>
    lateinit var acquisitionData: LiveData<AcquisitionData>

    fun loadItem(itemId: Int) {
        if (this.itemId == itemId) {
            return
        }

        this.itemId = itemId

        val lang = AppSettings.dataLocale
        item = dao.loadItem(lang, itemId)

        // Load item crafting, and split into "create" and "use"
        val craftingBase = dao.loadItemCombinationsFor(lang, itemId)
        val createRecipes = Transformations.map(craftingBase) {
            it.filter { it.result.id == itemId }
        }
        val useRecipes = Transformations.map(craftingBase) {
            it.filter { it.first.id == itemId || it.second?.id == itemId }
        }

        // todo: add more (merge instead)
        usageData = Transformations.map(useRecipes) { UsageData(it) }

        val itemLocations = dao.loadItemLocations(lang, itemId)
        acquisitionData = mergeLiveData(
                createRecipes,
                itemLocations) {
            AcquisitionData(it.first, it.second)
        }
    }
}
