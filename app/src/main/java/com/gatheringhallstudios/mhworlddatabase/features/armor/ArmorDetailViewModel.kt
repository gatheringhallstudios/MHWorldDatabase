package com.gatheringhallstudios.mhworlddatabase.features.armor

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.*

class ArmorDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).armorDao()

    private var armorId: Int = -1
    lateinit var armor: LiveData<ArmorFull>

    fun loadArmor(armorId: Int) {
        if (this.armorId == armorId) return

        armor = dao.loadArmorFull(AppSettings.dataLocale, armorId)
    }
}