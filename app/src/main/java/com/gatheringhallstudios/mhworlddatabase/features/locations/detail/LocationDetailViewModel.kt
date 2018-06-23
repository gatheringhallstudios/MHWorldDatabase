package com.gatheringhallstudios.mhworlddatabase.features.locations.detail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.LocationDao
import com.gatheringhallstudios.mhworlddatabase.data.views.LocationItemView
import com.gatheringhallstudios.mhworlddatabase.data.views.LocationView

class LocationDetailViewModel(application: Application) :  AndroidViewModel(application) {
    private val dao : LocationDao = MHWDatabase.getDatabase(application).locationDao()

    private var id: Int = 0
    private var initialized = false
    lateinit var locationItems : LiveData<List<LocationItemView>>
    lateinit var location : LiveData<LocationView>

    fun setLocation(locationid: Int) {
        if(initialized) {
            return
        }

        this.id = locationid
        location = dao.loadLocation(AppSettings.dataLocale, locationid)
        locationItems = dao.loadLocationItems(AppSettings.dataLocale, locationid)
        initialized = true
    }
}