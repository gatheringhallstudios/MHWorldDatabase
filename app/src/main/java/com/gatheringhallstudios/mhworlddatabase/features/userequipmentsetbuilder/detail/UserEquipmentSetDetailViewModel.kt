package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gatheringhallstudios.mhworlddatabase.data.AppDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class UserEquipmentSetDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val appDao = AppDatabase.getAppDataBase(application)!!.userEquipmentSetDao()

    var activeUserEquipmentSet = MutableLiveData<UserEquipmentSet>()
    private var activeUserEquipment : UserEquipment? = null // The userEquipment model being edited via armor/weapon/charm/decoration selector

    fun setActiveUserEquipment(userEquipment: UserEquipment?) {
        this.activeUserEquipment = userEquipment
    }

    fun isActiveUserEquipmentSetStale(): Boolean {
        return runBlocking {
            val updatedIds = withContext(Dispatchers.IO) { appDao.loadUserEquipmentSetIds(activeUserEquipmentSet.value!!.id) }

            try {
                val updatedUserEquipment = updatedIds.equipmentIds.first {
                    it.dataId == activeUserEquipment!!.entityId() &&
                            it.dataType == activeUserEquipment!!.type()
                }

                var decorations: List<Decoration> = listOf()
                when (activeUserEquipment!!.type()) {
                    DataType.ARMOR -> {
                        decorations = (activeUserEquipment as UserArmorPiece).decorations
                    }
                    DataType.WEAPON -> {
                        decorations = (activeUserEquipment as UserWeapon).decorations
                    }
                    else -> {
                       false //The piece of equipment is present and it has no decorations, i.e. data is not stale
                    }
                }

                if (decorations.size != updatedUserEquipment.decorationIds.size) true
                for (i in 1 until decorations.size) {
                    if (decorations[i].id != updatedUserEquipment.decorationIds[i].decorationId) true //Decorations don't match, i.e. data is stale
                }

                false

            } catch (exception: NoSuchElementException) {
                true //The piece of equipment is no longer present, i.e. the data is stale
            } catch (exception: NullPointerException) {
                 false //Active equipment Set or Active equipment hasn't been set yet, i.e. the data cannot be stale
            }
        }
    }
}