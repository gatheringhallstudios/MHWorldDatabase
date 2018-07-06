package com.gatheringhallstudios.mhworlddatabase.features.search

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.gatheringhallstudios.mhworlddatabase.common.ThrottledExecutor
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import kotlin.system.measureTimeMillis

// values are cached for 30 seconds
val timeout: Long = 30 * 1000

class UniversalSearchViewModel(app: Application) : AndroidViewModel(app) {
    private val TAG = javaClass.simpleName

    private val db = MHWDatabase.getDatabase(app)
    private val dao = db.searchDao()

    // this prevents search from being overwhelmed and makes everything orderly
    private val executor = ThrottledExecutor()

    var lastSearchFilter: String = ""
    val searchResults = MutableLiveData<List<Any>>()


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
        if (filterStr == "") {
            searchResults.postValue(emptyList())
            return
        }

        val results = mutableListOf<Any>()

        results.addAll(dao.searchLocations(filterStr))
        results.addAll(dao.searchMonsters(filterStr))
        results.addAll(dao.searchItems(filterStr))

        searchResults.postValue(results)
    }
}