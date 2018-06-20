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

fun makeSearchFilter(filter: String) : (String?) -> Boolean {
    val words = normalize(filter).split(',')
    return fun(test : String?) : Boolean {
        test ?: return false

        val testStr = normalize(test)
        for (word in words) {
            if (testStr.contains(word)) {
                return true
            }
        }

        return false
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

    fun searchData(filterString: String?) {
        val trimmedString = filterString?.trim() ?: ""
        val filter = makeSearchFilter(trimmedString)
        val lang = AppSettings.dataLocale

        executor.execute {
            if (trimmedString == "") {
                searchResults.postValue(listOf())
                return@execute
            }

            val results = mutableListOf<Any>()

            val monsterData = monsterDao.loadList(lang)
            val itemData = itemDao.loadItems(lang)

            results.addAll(monsterData.getResult().filter { filter(it.name) })
            results.addAll(itemData.getResult().filter { filter(it.name) })

            searchResults.postValue(results)
        }
    }
}