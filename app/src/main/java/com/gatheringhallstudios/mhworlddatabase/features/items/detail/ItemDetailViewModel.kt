package com.gatheringhallstudios.mhworlddatabase.features.items.detail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.gatheringhallstudios.mhworlddatabase.AppSettings

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.mergeLiveData



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

    lateinit var usageData: LiveData<ItemUsages>
    lateinit var acquisitionData: LiveData<ItemSources>

    fun loadItem(itemId: Int) {
        if (this.itemId == itemId) {
            return
        }

        this.itemId = itemId

        val lang = AppSettings.dataLocale
        item = dao.loadItem(lang, itemId)
        usageData = dao.loadItemUsagesFor(lang, itemId)
        acquisitionData = dao.loadItemSourcesFor(lang, itemId)
    }
}
