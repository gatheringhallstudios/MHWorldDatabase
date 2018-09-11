package com.gatheringhallstudios.mhworlddatabase.features.weapons.detail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.*

class WeaponDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).weaponDao()

//    private var armorId: Int = -1
//    lateinit var armor: LiveData<ArmorFull>
//
////    fun loadArmor(armorId: Int) {
////        if (this.armorId == armorId) return
////
////        armor = dao.loadArmorFull(AppSettings.dataLocale, armorId)
////    }
}