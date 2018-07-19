package com.gatheringhallstudios.mhworlddatabase.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.common.CachedValue
import com.gatheringhallstudios.mhworlddatabase.data.models.SearchResult
import com.gatheringhallstudios.mhworlddatabase.features.search.SearchFilter
import com.gatheringhallstudios.mhworlddatabase.features.search.timeout

@Dao
abstract class SearchDao {

    val locationDataCache = CachedValue(timeout) {
        val lang = AppSettings.dataLocale
        loadAllLocations(lang)
    }


    val monsterDataCache = CachedValue(timeout) {
        val lang = AppSettings.dataLocale
        loadAllMonsters(lang)
    }

    val itemDataCache = CachedValue(timeout) {
        val lang = AppSettings.dataLocale
        loadAllItems(lang)
    }

    fun searchLocations(searchFilter: String): List<SearchResult> {
        val filter = SearchFilter(searchFilter)
        return locationDataCache.get().filter { filter.matches(it.name) }
    }

    fun searchMonsters(searchFilter: String): List<SearchResult> {
        val filter = SearchFilter(searchFilter)
        return monsterDataCache.get().filter { filter.matches(it.name) }
    }

    fun searchItems(searchFilter: String): List<SearchResult> {
        val filter = SearchFilter(searchFilter)
        return itemDataCache.get().filter { filter.matches(it.name) }
    }


    @Query("""
        SELECT 'location' data_type, id, name
        FROM location_text
        WHERE lang_id = :langId
        ORDER BY name ASC
    """)
    protected abstract fun loadAllLocations(langId: String): List<SearchResult>

    @Query("""
        SELECT 'monster' data_type, m.id, mt.name
        FROM monster m
            JOIN monster_text mt USING (id)
            WHERE mt.lang_id = :langId
        ORDER BY mt.name ASC""")
    protected abstract fun loadAllMonsters(langId: String): List<SearchResult>

    @Query("""
        SELECT 'item' data_type, i.id, itext.name, i.icon_name, i.icon_color
        FROM item i
            JOIN item_text itext USING (id)
        WHERE itext.lang_id = :langId
        ORDER BY itext.name ASC
    """)
    protected abstract fun loadAllItems(langId: String): List<SearchResult>

}