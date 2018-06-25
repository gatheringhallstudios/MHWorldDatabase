package com.gatheringhallstudios.mhworlddatabase.features.locations.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.types.MonsterSize
import com.gatheringhallstudios.mhworlddatabase.data.views.LocationView
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView

/**
 * A viewmodel for any locations list fragment
 */
class LocationsListViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).locationDao()
    val locations = dao.loadLocations(AppSettings.dataLocale)
}
