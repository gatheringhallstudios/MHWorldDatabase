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

    private var weaponId: Int = -1
    lateinit var weapon: LiveData<WeaponFull>

    fun loadWeapon(weaponId: Int) {
        if (this.weaponId == weaponId) return

        weapon = dao.loadWeaponFull(AppSettings.dataLocale, weaponId)
    }
}