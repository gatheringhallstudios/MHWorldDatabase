package com.gatheringhallstudios.mhworlddatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.gatheringhallstudios.mhworlddatabase.common.StableMutableLiveData
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase

class MainActivityViewModel(app : Application) : AndroidViewModel(app) {
    /**
     * Livedata that contains the current accepted search view field value.
     * Updated whenever the searchview itself is updated.
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

    /**
     * Function that updates internal state to match a "new search".
     */
    fun startNewSearch() {
        // propogate state change. If statements prevent infinite recursion

        filter.value = ""
        searchActive.value = true
    }

    /**
     * Call this method when the search value has updated.
     * Recommended to be used by the owner of the search view
     */
    fun handleSearchUpdate(filterValue: String) {
        filter.value = filterValue
    }
}