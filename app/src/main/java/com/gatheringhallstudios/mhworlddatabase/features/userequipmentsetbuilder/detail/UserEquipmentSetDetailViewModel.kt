package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gatheringhallstudios.mhworlddatabase.data.AppDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.UserArmorPiece
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipment
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipmentSet
import com.gatheringhallstudios.mhworlddatabase.data.models.UserWeapon
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class UserEquipmentSetDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val appDao = AppDatabase.getAppDataBase(application)!!.userEquipmentSetDao()

    var activeUserEquipmentSet = MutableLiveData<UserEquipmentSet>()
    private var activeUserEquipment: UserEquipment? = null // The userEquipment model being edited via armor/weapon/charm/decoration selector

    fun setActiveUserEquipment(userEquipment: UserEquipment?) {
        this.activeUserEquipment = userEquipment
    }

    //This has to be blocking because the equipment set details screen cannot be rendered until this check is done
    fun isActiveUserEquipmentSetStale(): Boolean {
        if (activeUserEquipment == null) {
            return false //There is no active equipment, i.e. the data cannot be stale
        }

        return runBlocking {
            val storedIds = withContext(Dispatchers.IO) {
                appDao.loadSingleUserEquipmentId(activeUserEquipmentSet.value!!.id, activeUserEquipment!!.entityId(), activeUserEquipment!!.type())
            } ?: return@runBlocking true

            val decorations = when (activeUserEquipment!!.type()) {
                DataType.ARMOR -> {
                    (activeUserEquipment as UserArmorPiece).decorations
                }
                DataType.WEAPON -> {
                    (activeUserEquipment as UserWeapon).decorations
                }
                else -> {
                    return@runBlocking false //The piece of equipment is present and it has no decorations, i.e. data is not stale
                }
            }

            for (i in 1 until decorations.size) {
                if (decorations[i].decoration.id != storedIds.decorationIds[i].decorationId) return@runBlocking true //Piece of equipment matches, but Decorations don't match, i.e. data is stale
            }

            false
        }
    }
}
