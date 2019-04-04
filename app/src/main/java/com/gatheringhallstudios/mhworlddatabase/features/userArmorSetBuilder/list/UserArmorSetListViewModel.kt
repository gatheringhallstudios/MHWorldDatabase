package com.gatheringhallstudios.mhworlddatabase.features.userArmorSetBuilder.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

import com.gatheringhallstudios.mhworlddatabase.data.AppDatabase
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipmentSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Carlos on 3/22/2018.
 */

class UserArmorSetListViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).armorDao()
    private val appDao = AppDatabase.getAppDataBase(application)!!.userEquipmentSetDao()

    val userEquipmentSetData = MutableLiveData<MutableList<UserEquipmentSet>>()

    fun getEquipmentSetList() {
        GlobalScope.launch(Dispatchers.Main) {
            val equipmentSetLists = withContext(Dispatchers.IO) {
                appDao.loadUserEquipmentSets()
            }

            userEquipmentSetData.value = equipmentSetLists.toMutableList()
        }
    }
}
