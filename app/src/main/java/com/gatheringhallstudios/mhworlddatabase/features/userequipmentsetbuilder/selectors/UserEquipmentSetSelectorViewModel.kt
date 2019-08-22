package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.common.MHModelTreeFilter
import com.gatheringhallstudios.mhworlddatabase.data.AppDatabase
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.Armor
import com.gatheringhallstudios.mhworlddatabase.data.models.Charm
import com.gatheringhallstudios.mhworlddatabase.data.models.MHModelTree
import com.gatheringhallstudios.mhworlddatabase.data.models.Weapon
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserEquipmentSetSelectorViewModel(application: Application) : AndroidViewModel(application) {
    private val appDao = AppDatabase.getAppDataBase(application)!!.userEquipmentSetDao()
    private val charmDao = MHWDatabase.getDatabase(application).charmDao()
    private val weaponDao = MHWDatabase.getDatabase(application).weaponDao()
    private val armorDao = MHWDatabase.getDatabase(application).armorDao()

    var armor = MutableLiveData<List<Armor>>()
    lateinit var weapons: LiveData<List<Weapon>>
    lateinit var charms: LiveData<List<Charm>>

    init {
        loadArmor()
        loadCharms()
        loadWeapons()
    }

    fun filterArmor(filter: (Armor)-> Boolean) {
        armor.value = armor.value!!.filter {
            filter(it)
        }
    }

    private fun loadArmor() {
        GlobalScope.launch(Dispatchers.Main) {
            val armorList = withContext(Dispatchers.IO) {
                armorDao.loadArmorListSync(AppSettings.dataLocale)
            }

            armor.value = armorList
        }
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