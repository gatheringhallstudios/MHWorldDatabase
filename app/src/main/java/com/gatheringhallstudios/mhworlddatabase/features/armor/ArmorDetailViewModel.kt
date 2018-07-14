package com.gatheringhallstudios.mhworlddatabase.features.armor

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.ArmorDao
import com.gatheringhallstudios.mhworlddatabase.data.views.Armor

class ArmorDetailViewModel(application: Application) : AndroidViewModel(application) {
    private var armorId : Int = 0
    private val dao : ArmorDao = MHWDatabase.getDatabase(application).armorDao()

    lateinit var armor: LiveData<Armor>

    fun loadArmor(armorId: Int) {
        if(this.armorId == armorId) return

        armor = dao.loadArmor(AppSettings.dataLocale, armorId)
    }
}