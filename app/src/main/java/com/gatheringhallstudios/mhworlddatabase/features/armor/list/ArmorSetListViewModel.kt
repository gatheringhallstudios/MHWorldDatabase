package com.gatheringhallstudios.mhworlddatabase.features.armor.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.AppDatabase
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.Armor
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.models.ArmorSet
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType

/**
 * Created by Carlos on 3/22/2018.
 */

class ArmorSetListViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).armorDao()
    private val userEquipmentDao = AppDatabase.getAppDataBase(application)!!.userEquipmentSetDao()

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

    fun updateArmorPieceForArmorSet(newArmor: Armor, userEquipmentSetId: Int, prevId: Int?) {
        if (prevId != null) {
            userEquipmentDao.deleteUserEquipmentEquipment(prevId, DataType.ARMOR, userEquipmentSetId)
        }

        userEquipmentDao.createUserEquipmentEquipment(newArmor.id, DataType.ARMOR, userEquipmentSetId)
    }
}
