package com.gatheringhallstudios.mhworlddatabase.features.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import android.util.Log
import com.gatheringhallstudios.mhworlddatabase.util.ThrottledExecutor
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import kotlin.system.measureTimeMillis

class SearchResults(
        val locations: List<Location> = emptyList(),
        val monsters: List<MonsterBase> = emptyList(),
        val skillTrees: List<SkillTreeBase> = emptyList(),
        val charms: List<CharmBase> = emptyList(),
        val decorations: List<DecorationBase> = emptyList(),
        val armor: List<ArmorBase> = emptyList(),
        val items: List<ItemBase> = emptyList(),
        val weapons: List<WeaponBase> = emptyList(),
        val quests: List<QuestBase> = emptyList(),
        val kinsects: List<Kinsect> = emptyList(),
        val tools: List<ToolBase> = emptyList()
)

class UniversalSearchViewModel(app: Application) : AndroidViewModel(app) {
    private val TAG = javaClass.simpleName

    private val db = MHWDatabase.getDatabase(app)
    private val dao = db.searchDao()

    // this prevents search from being overwhelmed and makes everything orderly
    private val executor = ThrottledExecutor()

    // prevent double searching by storing the last search attempt
    // also used to check if the search should auto-open
    var searchFilter: String = ""
        private set

    // todo: Perhaps creating binders in the viewmodel isn't a good idea?
    // decide if we wanna do that, return a "search results" composite object, or some sort of typed data stream

    /**
     * Publicly exposed livedata containing the search results.
     * It is not recommended to update this directly, and instead observe.
     */
    val searchResults = MutableLiveData<SearchResults>()

    /**
     * Updates the search filter and begins searching.
     * SearchResults are populated when the search finishes.
     */
    fun searchData(filterString: String?) {
        val trimmedString = filterString?.trim() ?: ""
        if (trimmedString == searchFilter) {
            return
        }

        searchFilter = trimmedString

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
    private fun getResultsSync(filterStr: String): SearchResults {
        if (filterStr == "") {
            return SearchResults()
        }

        return SearchResults(
                locations = dao.searchLocations(filterStr),
                monsters = dao.searchMonsters(filterStr),
                skillTrees = dao.searchSkillTrees(filterStr),
                charms = dao.searchCharms(filterStr),
                decorations = dao.searchDecorations(filterStr),
                armor = dao.searchArmor(filterStr),
                items = dao.searchItems(filterStr),
                weapons = dao.searchWeapons(filterStr),
                quests = dao.searchQuests(filterStr),
                kinsects = dao.searchKinsects(filterStr),
                tools = dao.searchTools(filterStr)
        )
    }
}