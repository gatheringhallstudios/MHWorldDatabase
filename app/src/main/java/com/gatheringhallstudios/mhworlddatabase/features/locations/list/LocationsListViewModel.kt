package com.gatheringhallstudios.mhworlddatabase.features.locations.list

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.types.MonsterSize
import com.gatheringhallstudios.mhworlddatabase.data.views.LocationView
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView

/**
 * A viewmodel for any locations list fragment
 * Created by Carlos on 3/4/2018.
 */
class LocationsListViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).locationDao()

    lateinit var locations: LiveData<List<LocationView>>

    fun getLocations() {
        if (::locations.isInitialized) {
            return
        }

        locations = dao.loadLocations("en")
    }
}
