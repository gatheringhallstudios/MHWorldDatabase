package com.gatheringhallstudios.mhworlddatabase.features.search

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.common.ThrottledExecutor
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.getResult

fun normalize(str: String): String {
    var result = str.toLowerCase()
    result = result.replace("α", "alpha")
    result = result.replace("β", "beta")
    return result
}

class SearchFilter(private val filterString: String) {
    private val words = normalize(filterString).split(' ')

    fun matches(test: String?): Boolean {
        test ?: return false
        return matchesNormalized(normalize(test))
    }

    fun matchesNormalized(test: String?): Boolean {
        test ?: return false

        for (word in words) {
            if (!test.contains(word)) {
                return false
            }
        }

        return true
    }
}

class UniversalSearchViewModel(app: Application) : AndroidViewModel(app) {
    private val db = MHWDatabase.getDatabase(app)

    // todo: if we ever need a more efficient search mechanism, implement a robust search dao or repo
    private val monsterDao = db.monsterDao()
    private val itemDao = db.itemDao()

    // this prevents search from being overwhelmed and makes everything orderly
    private val executor = ThrottledExecutor()

    val searchResults = MutableLiveData<List<Any>>()

    val lang = AppSettings.dataLocale

    fun searchData(filterString: String?) {

        executor.execute {
            val trimmedString = filterString?.trim() ?: ""

            if (trimmedString == "") {
                searchResults.postValue(listOf())
                return@execute
            }

            val results = mutableListOf<Any>()

            val monsterData = monsterDao.loadList(lang).getResult()
            val itemData = itemDao.loadItems(lang).getResult()

            val filter = SearchFilter(trimmedString)
            results.addAll(monsterData.asSequence().filter { filter.matches(it.name) })
            results.addAll(itemData.asSequence().filter { filter.matches(it.name) })

            searchResults.postValue(results)
        }
    }
}