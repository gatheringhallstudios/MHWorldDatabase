package com.gatheringhallstudios.mhworlddatabase.features.items.detail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

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

        item = dao.loadItem("en", itemId)
        itemLocations = dao.loadItemLocations("en", itemId)
    }
}
