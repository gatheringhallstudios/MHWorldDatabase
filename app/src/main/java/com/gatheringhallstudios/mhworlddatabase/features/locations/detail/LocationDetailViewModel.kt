package com.gatheringhallstudios.mhworlddatabase.features.locations.detail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.LocationDao
import com.gatheringhallstudios.mhworlddatabase.data.models.LocationItem
import com.gatheringhallstudios.mhworlddatabase.data.models.Location
import com.gatheringhallstudios.mhworlddatabase.data.models.LocationCamp

class LocationDetailViewModel(application: Application) :  AndroidViewModel(application) {
    private val dao : LocationDao = MHWDatabase.getDatabase(application).locationDao()

    private var id: Int = 0
    lateinit var locationItems : LiveData<List<LocationItem>>
    lateinit var location : LiveData<Location>
    lateinit var camps: LiveData<List<LocationCamp>>

    fun setLocation(locationId: Int) {
        if (this.id == locationId) {
            return
        }

        this.id = locationId
        val lang = AppSettings.dataLocale
        location = dao.loadLocation(lang, locationId)
        locationItems = dao.loadLocationItems(lang, locationId)
        camps = dao.loadLocationCamps(lang, locationId)
    }
}