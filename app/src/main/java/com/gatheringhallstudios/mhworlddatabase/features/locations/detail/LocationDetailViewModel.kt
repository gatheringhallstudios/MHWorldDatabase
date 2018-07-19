package com.gatheringhallstudios.mhworlddatabase.features.locations.detail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.LocationDao
import com.gatheringhallstudios.mhworlddatabase.data.models.LocationItem
import com.gatheringhallstudios.mhworlddatabase.data.models.Location

class LocationDetailViewModel(application: Application) :  AndroidViewModel(application) {
    private val dao : LocationDao = MHWDatabase.getDatabase(application).locationDao()

    private var id: Int = 0
    lateinit var locationItems : LiveData<List<LocationItem>>
    lateinit var location : LiveData<Location>

    fun setLocation(locationId: Int) {
        if (this.id == locationId) {
            return
        }

        this.id = locationId
        location = dao.loadLocation(AppSettings.dataLocale, locationId)
        locationItems = dao.loadLocationItems(AppSettings.dataLocale, locationId)
    }
}