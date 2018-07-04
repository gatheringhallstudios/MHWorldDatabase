package com.gatheringhallstudios.mhworlddatabase.features.search

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.common.CachedValue
import com.gatheringhallstudios.mhworlddatabase.common.ThrottledExecutor
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.getResult
import kotlin.system.measureTimeMillis

// values are cached for 30 seconds
val timeout: Long = 30 * 1000

class UniversalSearchViewModel(app: Application) : AndroidViewModel(app) {
    private val TAG = javaClass.simpleName

    private val db = MHWDatabase.getDatabase(app)

    // todo: if we ever need a more efficient search mechanism, implement a robust search dao or repo
    private val locationDao = db.locationDao()
    private val monsterDao = db.monsterDao()
    private val itemDao = db.itemDao()

    // this prevents search from being overwhelmed and makes everything orderly
    private val executor = ThrottledExecutor()

    var lastSearchFilter: String = ""
    val searchResults = MutableLiveData<List<Any>>()

    val locationData = CachedValue(timeout) {
        val lang = AppSettings.dataLocale
        locationDao.loadLocations(lang).getResult()
    }

    val monsterData = CachedValue(timeout) {
        val lang = AppSettings.dataLocale
        monsterDao.loadList(lang).getResult()
    }

    val itemData = CachedValue(timeout) {
        val lang = AppSettings.dataLocale
        itemDao.loadItems(lang).getResult()
    }

    fun searchData(filterString: String?) {
        val trimmedString = filterString?.trim() ?: ""
        if (trimmedString == lastSearchFilter) {
            return
        }

        lastSearchFilter = trimmedString

        executor.execute {
            try {
                val time = measureTimeMillis {
                    handleSearch(trimmedString)
                }

                Log.d(TAG, "Search performed in $time milliseconds")
            } catch (ex: Exception) {
                Log.e(TAG, "Error while performing search", ex)
            }
        }
    }

    private fun handleSearch(filterStr: String) {
        val results = mutableListOf<Any>()

        val filter = SearchFilter(filterStr)
        results.addAll(locationData.get().asSequence().filter { filter.matches(it.name) })
        results.addAll(monsterData.get().asSequence().filter { filter.matches(it.name) })
        results.addAll(itemData.get().asSequence().filter { filter.matches(it.name) })

        searchResults.postValue(results)
    }
}