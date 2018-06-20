package com.gatheringhallstudios.mhworlddatabase

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.gatheringhallstudios.mhworlddatabase.common.StableMutableLiveData
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

    /**
     * Function that updates internal state to match a "new search".
     */
    fun startNewSearch() {
        // propogate state change. If statements prevent infinite recursion

        filter.value = ""
        searchActive.value = true
    }
}