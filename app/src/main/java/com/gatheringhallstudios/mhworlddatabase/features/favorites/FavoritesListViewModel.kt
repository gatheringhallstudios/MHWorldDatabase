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
class FavoritesListViewModel(app: Application) : AndroidViewModel(app) {
    private val armorDao = MHWDatabase.getDatabase(app).armorDao()
    private val itemDao = MHWDatabase.getDatabase(app).itemDao()

    lateinit var armorFavorites: LiveData<List<Armor>>
    lateinit var itemFavorites: LiveData<List<Item>>
    lateinit var decorationFavorites: LiveData<Decoration>
    lateinit var monsterFavorites : LiveData<Monster>
    lateinit var locationFavorites: LiveData<Location>
    lateinit var charmFavorites: LiveData<Charm>
    lateinit var skillFavorites: LiveData<SkillTree>
    lateinit var weaponFavorites: LiveData<Weapon>

    fun loadFavorites() {
        armorFavorites = armorDao.loadArmorByIdList(AppSettings.dataLocale, FavoritesFeature
                .getFavoritesByType(DataType.ARMOR).map {it.dataId}.toIntArray())
        itemFavorites = itemDao.loadItemsByIdList(AppSettings.dataLocale, FavoritesFeature
                .getFavoritesByType(DataType.ITEM).map {it.dataId}.toIntArray())

//        this.itemId = itemId
//
//        val lang = AppSettings.dataLocale
//        item = dao.loadItem(lang, itemId)
//        usageData = dao.loadItemUsagesFor(lang, itemId)
//        acquisitionData = dao.loadItemSourcesFor(lang, itemId)
    }
}
