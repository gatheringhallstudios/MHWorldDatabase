package com.gatheringhallstudios.mhworlddatabase.features.userEquipmentSetBuilder.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings

import com.gatheringhallstudios.mhworlddatabase.data.AppDatabase
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import kotlinx.coroutines.*

/**
 * Created by Carlos on 3/22/2018.
 */

class UserEquipmentSetListViewModel(application: Application) : AndroidViewModel(application) {
    private val decorationDao = MHWDatabase.getDatabase(application).decorationDao()
    private val charmDao = MHWDatabase.getDatabase(application).charmDao()
    private val weaponDao = MHWDatabase.getDatabase(application).weaponDao()
    private val armorDao = MHWDatabase.getDatabase(application).armorDao()
    private val appDao = AppDatabase.getAppDataBase(application)!!.userEquipmentSetDao()

    val userEquipmentSetId = MutableLiveData<MutableList<UserEquipmentSetIds>>()
    val userEquipmentSets = MutableLiveData<MutableList<UserEquipmentSet>>()

    fun getEquipmentSetList() {
        GlobalScope.launch(Dispatchers.Main) {
            val equipmentSetIds = withContext(Dispatchers.IO) {
                //                appDao.createUserEquipmentSet("test")
//                appDao.createUserEquipmentEequipment(1, DataType.ARMOR, 1)
//                appDao.createUserEquipmentEequipment(2, DataType.ARMOR, 1)
//                appDao.createUserEquipmentEequipment(3, DataType.ARMOR, 1)
//                appDao.createUserEquipmentEequipment(4, DataType.ARMOR, 1)
//                appDao.createUserEquipmentEequipment(634, DataType.ARMOR, 1)
//                appDao.deleteUserEquipmentEquipment()
                appDao.loadUserEquipmentSetIds()
            }

            val equipmentSets = withContext(Dispatchers.IO) {
                val deferred = equipmentSetIds.map {
                    async {
                        println("Doing on " + Thread.currentThread().name)
                        convertEquipmentSetIdToEquipmentSet(it)
                    }
                }

                deferred.map { it.await() }
            }

            userEquipmentSets.value = equipmentSets.toMutableList()
            userEquipmentSetId.value = equipmentSetIds.toMutableList()
        }
    }


    private fun convertEquipmentSetIdToEquipmentSet(userEquipmentSetIds: UserEquipmentSetIds): UserEquipmentSet {
        val userEquipment = mutableListOf<UserEquipment>()
        userEquipmentSetIds.equipmentIds.forEach { userEquipmentId ->
            when (userEquipmentId.dataType) {
                DataType.ARMOR -> {
                    val decorations = userEquipmentId.decorationIds.map { decorationFull ->
                        decorationDao.loadDecorationSync(AppSettings.dataLocale, decorationFull.decorationId)
                    }

                    userEquipment.add(UserArmorPiece(armor = armorDao.loadArmorFullSync(AppSettings.dataLocale, userEquipmentId.dataId),
                            decorations = decorations))
                }
                DataType.WEAPON -> {
                    val decorations = userEquipmentId.decorationIds.map { decorationFull ->
                        decorationDao.loadDecorationSync(AppSettings.dataLocale, decorationFull.decorationId)
                    }

                    userEquipment.add(UserWeapon(weapon = weaponDao.loadWeaponFullSync(AppSettings.dataLocale, userEquipmentId.dataId),
                            decorations = decorations))
                }
                DataType.CHARM -> {
                    userEquipment.add(UserCharm(charmDao.loadCharmFullSync(AppSettings.dataLocale, userEquipmentId.dataId)))
                }
                else -> {
                }
            }
        }

        val defense_base = userEquipment.sumBy { item ->
            if (item.getType() == DataType.ARMOR) {
                (item as UserArmorPiece).armor.armor.defense_base
            } else if (item.getType() == DataType.WEAPON) {
                (item as UserWeapon).weapon.weapon.defense
            } else 0
        }

        val defense_max = userEquipment.sumBy { item ->
            if (item.getType() == DataType.ARMOR) {
                (item as UserArmorPiece).armor.armor.defense_max
            } else if (item.getType() == DataType.WEAPON) {
                (item as UserWeapon).weapon.weapon.defense
            } else 0
        }

        val defense_augment_max = userEquipment.sumBy { item ->
            if (item.getType() == DataType.ARMOR) {
                (item as UserArmorPiece).armor.armor.defense_augment_max
            } else if (item.getType() == DataType.WEAPON) {
                (item as UserWeapon).weapon.weapon.defense
            } else 0
        }

        val fireDefense = userEquipment.sumBy { item ->
            if (item.getType() == DataType.ARMOR) {
                (item as UserArmorPiece).armor.armor.fire
            } else 0
        }

        val waterDefense = userEquipment.sumBy { item ->
            if (item.getType() == DataType.ARMOR) {
                (item as UserArmorPiece).armor.armor.water
            } else 0
        }


        val thunderDefense = userEquipment.sumBy { item ->
            if (item.getType() == DataType.ARMOR) {
                (item as UserArmorPiece).armor.armor.thunder
            } else 0
        }

        val iceDefense = userEquipment.sumBy { item ->
            if (item.getType() == DataType.ARMOR) {
                (item as UserArmorPiece).armor.armor.ice
            } else 0
        }

        val dragonDefense = userEquipment.sumBy { item ->
            if (item.getType() == DataType.ARMOR) {
                (item as UserArmorPiece).armor.armor.dragon
            } else 0
        }

        val skillLevels = mutableMapOf<Int, SkillLevel>()
        userEquipment.forEach { item ->
            val providedSkills = mutableListOf<SkillLevel>()
            when (item.getType()) {
                DataType.ARMOR -> {
                    (item as UserArmorPiece).decorations.forEach { decoration ->
                        val skillLevel = SkillLevel(1) //Decorations always only give 1 skill point
                        skillLevel.skillTree = decoration.skillTree
                        providedSkills.add(skillLevel)
                    }

                    providedSkills.addAll(item.armor.skills)
                }

                DataType.WEAPON -> {
                    (item as UserWeapon).decorations.forEach { decoration ->
                        val skillLevel = SkillLevel(1) //Decorations always only give 1 skill point
                        skillLevel.skillTree = decoration.skillTree
                        providedSkills.add(skillLevel)
                    }
                }
                DataType.CHARM -> {
                    providedSkills.addAll((item as UserCharm).charm.skills)
                }
                else -> {
                }
            }

            providedSkills.forEach {
                if (skillLevels.containsKey(it.skillTree.id)) {
                    val level = skillLevels[it.skillTree.id]!!.level + it.level
                    val skillLevel = SkillLevel(level)
                    skillLevel.skillTree = it.skillTree
                    skillLevels[it.skillTree.id] = skillLevel
                } else {
                    skillLevels[it.skillTree.id] = it
                }
            }
        }

        return {
            val set = UserEquipmentSet(userEquipmentSetIds.id, userEquipmentSetIds.name, userEquipment)
            set.defense_base = defense_base
            set.defense_max = defense_max
            set.defense_augment_max = defense_augment_max
            set.fireDefense = fireDefense
            set.waterDefense = waterDefense
            set.thunderDefense = thunderDefense
            set.iceDefense = iceDefense
            set.dragonDefense = dragonDefense
            set.skills = skillLevels

            set
        }()
    }
}
