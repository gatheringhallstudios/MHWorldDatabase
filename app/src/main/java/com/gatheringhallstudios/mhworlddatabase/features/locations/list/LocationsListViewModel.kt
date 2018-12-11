package com.gatheringhallstudios.mhworlddatabase.features.locations.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.gatheringhallstudios.mhworlddatabase.AppSettings

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase

/**
 * A viewmodel for any locations list fragment
 */
class LocationsListViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).locationDao()
    val locations = dao.loadLocations(AppSettings.dataLocale)
}
