package com.gatheringhallstudios.mhworlddatabase.features.userArmorSetBuilder.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.models.ArmorSet

/**
 * Created by Carlos on 3/22/2018.
 */

class UserArmorSetListViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).armorDao()

    private var rank: Rank? = null
    private lateinit var armorSetData: LiveData<List<ArmorSet>>

    fun getArmorSetList(rank: Rank?): LiveData<List<ArmorSet>> {
        if (this.rank == rank && ::armorSetData.isInitialized) {
            return armorSetData
        }

        this.rank = rank
        this.armorSetData = dao.loadArmorSets(AppSettings.dataLocale, rank)
        return armorSetData
    }
}
