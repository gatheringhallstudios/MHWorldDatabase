package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.selectors

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.data.AppDatabase
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType

class UserEquipmentSetSelectorViewModel(application: Application) : AndroidViewModel(application) {
    private val appDao = AppDatabase.getAppDataBase(application)!!.userEquipmentSetDao()
    private val charmDao = MHWDatabase.getDatabase(application).charmDao()
    private val weaponDao = MHWDatabase.getDatabase(application).weaponDao()
    private val armorDao = MHWDatabase.getDatabase(application).armorDao()
    private val decorationDao = MHWDatabase.getDatabase(application).decorationDao()

    lateinit var armor: LiveData<List<ArmorFull>>
    lateinit var weapons: LiveData<List<WeaponFull>>
    lateinit var decorations: LiveData<List<Decoration>>
    lateinit var charms: LiveData<List<CharmFull>>

//    init {
//        loadArmor()
//        loadCharms()
//        loadWeapons()
//    }

//    fun filterArmor(filter: (Armor)-> Boolean) {
//        armor.value = armor.value!!.filter {
//            filter(it)
//        }
//    }

    fun loadArmor(langId: String, armorType: ArmorType) {
        armor = armorDao.loadArmorFullByType(langId, armorType)
    }

    fun loadCharms(langId: String) {
        charms = charmDao.loadCharmAndSkillList(langId)
    }

    fun loadDecorations(langId: String) {
        decorations = decorationDao.loadDecorationsWithSkills(langId)
    }

    fun loadWeapons(langId: String) {
        this.weapons = weaponDao.loadWeaponsWithSkillsSync(langId)
    }

//    fun loadCharms() {
//        if (::charms.isInitialized) {
//            return
//        }
//
//        this.charms = charmDao.loadCharmList(AppSettings.dataLocale)
//    }

    fun updateEquipmentForEquipmentSet(newId: Int, type: DataType, userEquipmentSetId: Int, prevId: Int?) {
        if (prevId != null) {
            appDao.deleteUserEquipmentEquipment(prevId, type, userEquipmentSetId)
            appDao.deleteUserEquipmentDecorations(userEquipmentSetId, prevId, type)
        }

        appDao.createUserEquipmentEquipment(newId, type, userEquipmentSetId)
    }

    fun updateDecorationForEquipmentSet(newId: Int, targetDataId: Int, targetSlotNumber: Int, type: DataType, userEquipmentSetId: Int, prevId: Int?) {
        if (prevId != null) {
            appDao.deleteUserEquipmentDecoration(userEquipmentSetId, targetDataId, type, prevId, targetSlotNumber)
        }

        appDao.createUserEquipmentDecoration(userEquipmentSetId, targetDataId, targetSlotNumber, type, newId)
    }
}