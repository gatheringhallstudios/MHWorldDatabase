package com.gatheringhallstudios.mhworlddatabase.features.weapons.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.*

/**
 * Viewmodel used for weapon detail data
 */
class WeaponDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).weaponDao()

    var weaponId: Int = -1
        private set

    lateinit var weapon: LiveData<WeaponFull>
    lateinit var weaponTrees: LiveData<WeaponTreeCollection>

    fun loadWeapon(weaponId: Int) {
        if (this.weaponId == weaponId) return

        this.weaponId = weaponId

        val langId = AppSettings.dataLocale
        weapon = dao.loadWeaponFull(langId, weaponId)
        weaponTrees = Transformations.map(weapon) {
            dao.loadWeaponTrees(langId, it.weapon.weapon_type)
        }
    }
}