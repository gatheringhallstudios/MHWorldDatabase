package com.gatheringhallstudios.mhworlddatabase.features.locations.detail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.LocationDao
import com.gatheringhallstudios.mhworlddatabase.data.views.LocationItemView
import com.gatheringhallstudios.mhworlddatabase.data.views.LocationView

class LocationDetailViewModel(application: Application) :  AndroidViewModel(application) {
    private val dao : LocationDao

    init{
        val db = MHWDatabase.getDatabase(application)
        dao = db.locationDao()
    }

    private var id: Int = 0
    private var initialized = false
    lateinit var locationItems : LiveData<List<LocationItemView>>
    lateinit var location : LiveData<LocationView>

    fun setLocation(locationid: Int) {
        if(initialized) {
            return
        }

        this.id = locationid
        location = dao.loadLocation("en", locationid)
        locationItems = dao.loadLocationItems("en", locationid)
        initialized = true
    }
}