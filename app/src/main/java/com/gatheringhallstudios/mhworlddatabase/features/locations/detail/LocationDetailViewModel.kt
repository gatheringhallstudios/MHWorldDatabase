package com.gatheringhallstudios.mhworlddatabase.features.locations.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.LocationDao
import com.gatheringhallstudios.mhworlddatabase.data.views.LocationItemView
import com.gatheringhallstudios.mhworlddatabase.data.views.LocationView

class LocationDetailViewModel(application: Application) :  AndroidViewModel(application) {
    private val dao : LocationDao = MHWDatabase.getDatabase(application).locationDao()

    private var id: Int = 0
    lateinit var locationItems : LiveData<List<LocationItemView>>
    lateinit var location : LiveData<LocationView>

    fun setLocation(locationId: Int) {
        if (this.id == locationId) {
            return
        }

        this.id = locationId
        location = dao.loadLocation(AppSettings.dataLocale, locationId)
        locationItems = dao.loadLocationItems(AppSettings.dataLocale, locationId)
    }
}