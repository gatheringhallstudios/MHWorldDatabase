package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.AppDatabase
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.Armor
import com.gatheringhallstudios.mhworlddatabase.data.models.Charm
import com.gatheringhallstudios.mhworlddatabase.data.models.Weapon
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType

class UserEquipmentSetSelectorViewModel(application: Application) : AndroidViewModel(application) {
    private val appDao = AppDatabase.getAppDataBase(application)!!.userEquipmentSetDao()
    private val charmDao = MHWDatabase.getDatabase(application).charmDao()
    private val weaponDao = MHWDatabase.getDatabase(application).weaponDao()
    private val armorDao = MHWDatabase.getDatabase(application).armorDao()

    lateinit var armor: LiveData<List<Armor>>
    lateinit var weapons: LiveData<List<Weapon>>
    lateinit var charms: LiveData<List<Charm>>

    fun init() {
        loadArmor()
        loadCharms()
        loadWeapons()
    }
    fun loadArmor() {
        if (::armor.isInitialized) {
            return
        }

        this.armor = armorDao.loadArmorList(AppSettings.dataLocale, Rank.HIGH)
    }

    fun loadWeapons() {
//        if (::weapons.isInitialized) {
//            return
//        }
//
//        this.weapons = weaponDao.loadWeaponTrees(AppSettings.dataLocale, WeaponType.BOW)
    }

    fun loadCharms() {
        if (::charms.isInitialized) {
            return
        }

        this.charms = charmDao.loadCharmList(AppSettings.dataLocale)
    }

    fun updateArmorPieceForArmorSet(newArmor: Armor, userEquipmentSetId: Int, prevId: Int?) {
        if (prevId != null) {
            appDao.deleteUserEquipmentEquipment(prevId, DataType.ARMOR, userEquipmentSetId)
        }

        appDao.createUserEquipmentEquipment(newArmor.id, DataType.ARMOR, userEquipmentSetId)
    }
}