package com.gatheringhallstudios.mhworlddatabase.features.search

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.gatheringhallstudios.mhworlddatabase.adapters.SimpleUniversalBinder
import com.gatheringhallstudios.mhworlddatabase.common.ThrottledExecutor
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import kotlin.system.measureTimeMillis

class UniversalSearchViewModel(app: Application) : AndroidViewModel(app) {
    private val TAG = javaClass.simpleName

    private val db = MHWDatabase.getDatabase(app)
    private val dao = db.searchDao()

    // this prevents search from being overwhelmed and makes everything orderly
    private val executor = ThrottledExecutor()

    // prevent double searching by storing the last search attempt
    // also used to check if the search should auto-open
    var lastSearchFilter: String = ""

    // todo: Perhaps creating binders in the viewmodel isn't a good idea?
    // decide if we wanna do that, return a "search results" composite object, or some sort of typed data stream

    /**
     * Publicly exposed livedata containing the search results.
     * It is not recommended to update this directly, and instead observe.
     */
    val searchResults = MutableLiveData<List<SimpleUniversalBinder>>()

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
    private fun getResultsSync(filterStr: String): List<SimpleUniversalBinder> {
        if (filterStr == "") {
            return emptyList()
        }

        val results = mutableListOf<SimpleUniversalBinder>()

        results.addAll(dao.searchLocations(filterStr).map(::createLocationBinder))
        results.addAll(dao.searchMonsters(filterStr).map(::createMonsterBinder))
        results.addAll(dao.searchSkillTrees(filterStr).map(::createSkillTreeBinder))
        results.addAll(dao.searchCharms(filterStr).map(::createCharmBinder))
        results.addAll(dao.searchDecorations(filterStr).map(::createDecorationBinder))
        results.addAll(dao.searchArmor(filterStr).map(::createArmorBinder))
        results.addAll(dao.searchItems(filterStr).map(::createItemBinder))

        return results
    }
}