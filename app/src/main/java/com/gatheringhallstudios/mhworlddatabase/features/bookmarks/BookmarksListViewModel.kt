package com.gatheringhallstudios.mhworlddatabase.features.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.AppDatabase

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType


/**
 * A viewmodel that contains information for a single item.
 * This viewmodel should be attached to the pager fragment,
 * and accessed through the parent for sub-fragments
 */
class BookmarksListViewModel(app: Application) : AndroidViewModel(app) {
    private val favoriteDao = MHWDatabase.getDatabase(app).favoritesSearchDao()

    lateinit var  favoriteEntities: LiveData<FavoriteEntities>

    fun loadFavorites() {
        favoriteEntities = favoriteDao.getFavoriteEntities(AppSettings.dataLocale,
                FavoritesFeature
                        .getFavoritesByType(DataType.ARMOR).map {it.dataId}.toIntArray(),
                FavoritesFeature
                        .getFavoritesByType(DataType.CHARM).map {it.dataId}.toIntArray(),
                FavoritesFeature
                        .getFavoritesByType(DataType.ITEM).map {it.dataId}.toIntArray(),
                FavoritesFeature
                        .getFavoritesByType(DataType.LOCATION).map {it.dataId}.toIntArray(),
                FavoritesFeature
                        .getFavoritesByType(DataType.MONSTER).map {it.dataId}.toIntArray(),
                FavoritesFeature
                        .getFavoritesByType(DataType.SKILL).map {it.dataId}.toIntArray(),
                FavoritesFeature
                        .getFavoritesByType(DataType.WEAPON).map {it.dataId}.toIntArray(),
                FavoritesFeature
                        .getFavoritesByType(DataType.DECORATION).map {it.dataId}.toIntArray()
                )
    }
}
