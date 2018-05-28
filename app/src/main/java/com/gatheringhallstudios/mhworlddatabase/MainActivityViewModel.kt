package com.gatheringhallstudios.mhworlddatabase

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase

class MainActivityViewModel(app : Application) : AndroidViewModel(app) {
    /**
     * Livedata that contains the current searchview field value.
     * Update to propogate that data
     */
    val filter = StableMutableLiveData<String>()

    /**
     * LiveData that contains whether the search is open or not
     * Update to open/close the search view
     */
    val searchActive = StableMutableLiveData<Boolean>()

    init {
        this.filter.value = ""
        this.searchActive.value = false
    }

    // the below is a temporary search implementation
    // we may or may not move the implementation of searching to the UniversalSearchFragment

    // the data loaded by this is temporary for testing reasons
    // eventually we'll make a search dao
    val dao = MHWDatabase.getDatabase(app).monsterDao()

    /**
     * Observe to retrieve search results
     */
    val searchResults : LiveData<List<Any>> = Transformations.switchMap(filter) {
        getNewDataFor(it ?: "")
    }

    /**
     * Function that updates internal state to match a "new search".
     */
    fun startNewSearch() {
        // propogate state change. If statements prevent infinite recursion

        filter.value = ""
        searchActive.value = true
    }

    // temporary implementation
    private fun getNewDataFor(search : String) : LiveData<List<Any>>? {
        // return empty results if search is empty
        if (search.trim() == "") {
            val emptyData = MutableLiveData<List<Any>>()
            emptyData.value = ArrayList()
            return emptyData
        }

        return dao.loadList("en") as LiveData<List<Any>>
    }
}