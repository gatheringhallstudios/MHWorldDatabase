package com.gatheringhallstudios.mhworlddatabase.features.userEquipmentSetBuilder.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings

import com.gatheringhallstudios.mhworlddatabase.data.AppDatabase
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.Armor
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipmentSet
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Carlos on 3/22/2018.
 */

class UserEquipmentSetListViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).armorDao()
    private val appDao = AppDatabase.getAppDataBase(application)!!.userEquipmentSetDao()

    val userEquipmentSetData = MutableLiveData<MutableList<UserEquipmentSet>>()

    fun getEquipmentSetList() {
        GlobalScope.launch(Dispatchers.Main) {
            val equipmentSetLists = withContext(Dispatchers.IO) {
                println("Done!")
                appDao.loadUserEquipmentSets()
            }

            withContext(Dispatchers.IO) {
                println("Done2!")

                equipmentSetLists.forEach {
                    val equipment = mutableListOf<Armor>()
                    it.equipment.forEach { userEquipment ->
                        if (userEquipment.dataType == DataType.ARMOR) {
                            equipment.add(dao.loadArmorSync(AppSettings.dataLocale, userEquipment.dataId))
                        }
                    }
                    it.defense_base = equipment.sumBy { armor -> armor.defense_base}
                    it.defense_max = equipment.sumBy { armor -> armor.defense_max}
                    it.defense_augment_max = equipment.sumBy { armor -> armor.defense_augment_max}
                    it.fireDefense = equipment.sumBy { armor -> armor.fire}
                    it.waterDefense = equipment.sumBy { armor -> armor.water}
                    it.thunderDefense = equipment.sumBy { armor -> armor.thunder}
                    it.iceDefense = equipment.sumBy { armor -> armor.ice}
                    it.dragonDefense = equipment.sumBy { armor -> armor.dragon}
                }
            }
            println("Done3!")
            userEquipmentSetData.value = equipmentSetLists.toMutableList()
        }
    }
}
