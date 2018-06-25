package com.gatheringhallstudios.mhworlddatabase.features.items.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.views.ItemLocationView
import com.gatheringhallstudios.mhworlddatabase.data.views.ItemView

/**
 * A viewmodel that contains information for a single item.
 * This viewmodel should be attached to the pager fragment,
 * and accessed through the parent for sub-fragments
 */
class ItemDetailViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = MHWDatabase.getDatabase(app).itemDao()

    var itemId = -1
        private set

    lateinit var item: LiveData<ItemView>
    lateinit var itemLocations: LiveData<List<ItemLocationView>>

    fun loadItem(itemId: Int) {
        if (this.itemId == itemId) {
            return
        }

        this.itemId = itemId

        val lang = AppSettings.dataLocale
        item = dao.loadItem(lang, itemId)
        itemLocations = dao.loadItemLocations(lang, itemId)
    }
}
