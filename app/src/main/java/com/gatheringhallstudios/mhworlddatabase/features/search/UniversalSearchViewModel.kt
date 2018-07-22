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

    // prevent double searching by storing the last search attempt
    // also used to check if the search should auto-open
    var lastSearchFilter: String = ""

    /**
     * Publicly exposed livedata containing the search results.
     * It is not recommended to update this directly, and instead observe.
     */
    val searchResults = MutableLiveData<List<Any>>()

    /**
     * Updates the search filter and begins searching.
     * SearchResults are populated when the search finishes.
     */
    fun searchData(filterString: String?) {
        val trimmedString = filterString?.trim() ?: ""
        if (trimmedString == lastSearchFilter) {
            return
        }

        lastSearchFilter = trimmedString

        executor.execute {
            try {
                val time = measureTimeMillis {
                    val results = getResultsSync(trimmedString)
                    searchResults.postValue(results)
                }

                Log.d(TAG, "Search performed in $time milliseconds")
            } catch (ex: Exception) {
                Log.e(TAG, "Error while performing search", ex)
            }
        }
    }

    /**
     * Internal helper that retrieves search results synchronously.
     * Run this on a background thread.
     */
    private fun getResultsSync(filterStr: String): List<Any> {
        if (filterStr == "") {
            return emptyList()
        }

        val results = mutableListOf<Any>()

        results.addAll(dao.searchLocations(filterStr))
        results.addAll(dao.searchMonsters(filterStr))
        results.addAll(dao.searchItems(filterStr))

        return results
    }
}