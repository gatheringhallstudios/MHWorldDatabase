package com.gatheringhallstudios.mhworlddatabase.features.items.detail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.views.ItemView

class ItemDetailViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = MHWDatabase.getDatabase(app).itemDao()

    var itemId = -1
        private set

    lateinit var item: LiveData<ItemView>


    fun loadItem(itemId: Int) {
        if (this.itemId == itemId) {
            return
        }

        this.itemId = itemId

        item = dao.loadItem("en", itemId)

    }
}
