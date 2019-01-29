package com.gatheringhallstudios.mhworlddatabase.features.bookmarks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType


/**
 * A viewmodel that contains information for a single item.
 * This viewmodel should be attached to the pager fragment,
 * and accessed through the parent for sub-fragments
 */
class BookmarksListViewModel(app: Application) : AndroidViewModel(app) {
    private val bookmarkDao = MHWDatabase.getDatabase(app).bookmarksSearchDao()

    lateinit var  bulkModels: LiveData<BulkModels>

    fun loadBookmarks() {
        bulkModels = bookmarkDao.getModels(AppSettings.dataLocale,
                BookmarksFeature
                        .getBookmarksByType(DataType.ARMOR).map {it.dataId}.toIntArray(),
                BookmarksFeature
                        .getBookmarksByType(DataType.CHARM).map {it.dataId}.toIntArray(),
                BookmarksFeature
                        .getBookmarksByType(DataType.ITEM).map {it.dataId}.toIntArray(),
                BookmarksFeature
                        .getBookmarksByType(DataType.LOCATION).map {it.dataId}.toIntArray(),
                BookmarksFeature
                        .getBookmarksByType(DataType.MONSTER).map {it.dataId}.toIntArray(),
                BookmarksFeature
                        .getBookmarksByType(DataType.SKILL).map {it.dataId}.toIntArray(),
                BookmarksFeature
                        .getBookmarksByType(DataType.WEAPON).map {it.dataId}.toIntArray(),
                BookmarksFeature
                        .getBookmarksByType(DataType.DECORATION).map {it.dataId}.toIntArray()
                )
    }
}
