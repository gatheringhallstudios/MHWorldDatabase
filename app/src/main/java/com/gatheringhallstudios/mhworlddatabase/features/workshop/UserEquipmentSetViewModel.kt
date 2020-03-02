package com.gatheringhallstudios.mhworlddatabase.features.workshop

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.AppDatabase
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import kotlinx.coroutines.*

class UserEquipmentSetViewModel(application: Application) : AndroidViewModel(application) {
    private val appDao = AppDatabase.getAppDataBase(application)!!.userEquipmentSetDao()
    private val decorationDao = MHWDatabase.getDatabase(application).decorationDao()
    private val charmDao = MHWDatabase.getDatabase(application).charmDao()
    private val weaponDao = MHWDatabase.getDatabase(application).weaponDao()
    private val armorDao = MHWDatabase.getDatabase(application).armorDao()


    private var _activeUserEquipmentSet = MutableLiveData<UserEquipmentSet>()
    private var _userEquipmentSets = MutableLiveData<MutableList<UserEquipmentSet>>()


    val activeUserEquipmentSet: LiveData<UserEquipmentSet> = _activeUserEquipmentSet
    val userEquipmentSets: LiveData<MutableList<UserEquipmentSet>> = _userEquipmentSets
    var activeUserEquipment: UserEquipment? = null // The userEquipment model being edited via armor/weapon/charm/decoration selector

    //Keeps track of the state of equipment cards on the armor set edit fragment
    private var _armorSetCardStates = mutableMapOf(0 to false, 1 to false, 2 to false, 3 to false, 4 to false, 5 to false, 6 to false)
    val armorSetCardStates: Map<Int, Boolean> = _armorSetCardStates

    fun updateCardState(cardIndex: Int, state: Boolean) {
        _armorSetCardStates[cardIndex] = state
    }

    fun resetCardStates() {
        for ((key, _) in _armorSetCardStates) {
            _armorSetCardStates[key] = false
        }
    }

    fun setActiveUserEquipmentSet(id: Int) {
        runBlocking {
            val set = async { getEquipmentSet(id) }
            _activeUserEquipmentSet.value = set.await()
        }
    }

    fun deleteDecorationForEquipment(decorationId: Int, targetDataId: Int, targetSlotNumber: Int, type: DataType, userEquipmentSetId: Int) {
        val deferredResult = GlobalScope.async {
            withContext(Dispatchers.IO) {
                appDao.deleteUserEquipmentDecoration(userEquipmentSetId, targetDataId, type, decorationId, targetSlotNumber)
                val equipmentSetIds = appDao.loadUserEquipmentSetIds(userEquipmentSetId)
                convertEquipmentSetIdToEquipmentSet(equipmentSetIds)
            }
        }

        runBlocking {
            _activeUserEquipmentSet.value = deferredResult.await()
        }
    }

    fun deleteEquipmentSet(userEquipmentSetId: Int) {
        runBlocking {
            withContext(Dispatchers.IO) {
                appDao.deleteUserEquipmentSet(userEquipmentSetId)
                appDao.deleteUserEquipmentSetEquipment(userEquipmentSetId)
                appDao.deleteUserEquipmentSetDecorations(userEquipmentSetId)
            }
        }
    }

    fun renameEquipmentSet(name: String, userEquipmentSetId: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                appDao.renameUserEquipmentSet(name, userEquipmentSetId)
            }
            _activeUserEquipmentSet.value = getEquipmentSet(userEquipmentSetId)
        }
    }

    fun deleteUserEquipment(userEquipmentId: Int, userEquipmentSetId: Int, type: DataType) {
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                appDao.deleteUserEquipmentEquipment(userEquipmentId, type, userEquipmentSetId)
                appDao.deleteUserEquipmentDecorations(userEquipmentSetId, userEquipmentId, type)
            }
            _activeUserEquipmentSet.value = getEquipmentSet(userEquipmentSetId)
        }
    }

    /*
    Creates a new equipment set and sets that as the active equipment set
     */
    fun createEquipmentSet(): UserEquipmentSet {
        return runBlocking {
            val set = withContext(Dispatchers.IO) {
                val newId = appDao.createUserEquipmentSet("New Set")
                convertEquipmentSetIdToEquipmentSet(appDao.loadUserEquipmentSetIds(newId.toInt()))
            }
            _activeUserEquipmentSet.value = set
            set
        }
    }

    fun deleteEquipmentSet(userEquipmentSet: UserEquipmentSet) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                appDao.deleteUserEquipmentSet(userEquipmentSet.id)
                appDao.deleteUserEquipmentSetEquipment(userEquipmentSet.id)
                appDao.deleteUserEquipmentSetDecorations(userEquipmentSet.id)
            }
        }
    }

    fun getEquipmentSets() {
        GlobalScope.launch {
            val equipmentSetIds = withContext(Dispatchers.IO) {
                appDao.loadUserEquipmentSetIds()
            }

            val equipmentSets = withContext(Dispatchers.IO) {
                val deferred = equipmentSetIds.map {
                    async {
                        convertEquipmentSetIdToEquipmentSet(it)
                    }
                }

                deferred.map { it.await() }
            }

            withContext(Dispatchers.Main) {
                _userEquipmentSets.value = equipmentSets.toMutableList()
            }
        }
    }

    fun getEquipmentSet(equipmentSetId: Int): UserEquipmentSet {
        return runBlocking {
            val equipmentSet = withContext(Dispatchers.IO) {
                val equipmentSetIds = appDao.loadUserEquipmentSetIds(equipmentSetId)
                convertEquipmentSetIdToEquipmentSet(equipmentSetIds)
            }
            equipmentSet
        }
    }

    private fun convertEquipmentSetIdToEquipmentSet(userEquipmentSetIds: UserEquipmentSetIds): UserEquipmentSet {
        val userEquipment = mutableListOf<UserEquipment>()
        userEquipmentSetIds.equipmentIds.forEach { userEquipmentId ->
            when (userEquipmentId.dataType) {
                DataType.ARMOR -> {
                    val decorations = userEquipmentId.decorationIds.map { decorationIds ->
                        UserDecoration(
                                equipmentSetId = userEquipmentSetIds.id,
                                decoration = decorationDao.loadDecorationSync(AppSettings.dataLocale, decorationIds.decorationId),
                                slotNumber = decorationIds.slotNumber)
                    }.sortedWith(compareBy(UserDecoration::slotNumber))
                    userEquipment.add(UserArmorPiece(
                            equipmentSetId = userEquipmentSetIds.id,
                            armor = armorDao.loadArmorFullSync(AppSettings.dataLocale, userEquipmentId.dataId),
                            decorations = decorations))
                }
                DataType.WEAPON -> {
                    val decorations = userEquipmentId.decorationIds.map { decorationIds ->
                        UserDecoration(
                                equipmentSetId = userEquipmentSetIds.id,
                                decoration = decorationDao.loadDecorationSync(AppSettings.dataLocale, decorationIds.decorationId),
                                slotNumber = decorationIds.slotNumber)
                    }.toMutableList()
                    userEquipment.add(UserWeapon(
                            equipmentSetId = userEquipmentSetIds.id,
                            weapon = weaponDao.loadWeaponFullSync(AppSettings.dataLocale, userEquipmentId.dataId),
                            decorations = decorations))
                }
                DataType.CHARM -> {
                    userEquipment.add(UserCharm(
                            equipmentSetId = userEquipmentSetIds.id,
                            charm = charmDao.loadCharmFullSync(AppSettings.dataLocale, userEquipmentId.dataId)))
                }
                else -> {
                } //Shouldn't happen, so ignore
            }
        }

        return {
            val set = UserEquipmentSet(userEquipmentSetIds.id, userEquipmentSetIds.name, userEquipment)
            set.defense_base = calculateDefenseBase(userEquipment)
            set.defense_max = calculateDefenseMax(userEquipment)
            set.defense_augment_max = calculateDefenseAugMax(userEquipment)
            set.fireDefense = calculateFireDefense(userEquipment)
            set.waterDefense = calculateWaterDefense(userEquipment)
            set.thunderDefense = calculateThunderDefense(userEquipment)
            set.iceDefense = calculateIceDefense(userEquipment)
            set.dragonDefense = calculateDragonDefense(userEquipment)
            set.setBonuses = calculateSetBonuses(userEquipment)
            set.skills = calculateSkillLevels(userEquipment,
                    set.setBonuses.map { it.value.mapNotNull { itr -> itr.skillTree.unlocks_id } }.flatten().toSet())
            set.maxRarity = calculateMaxRarity(userEquipment)

            set
        }()
    }

    private fun calculateMaxRarity(userEquipment: List<UserEquipment>): Int {
        var maxRarity = 0
        userEquipment.forEach {
            var rarity = 0
            when (it.type()) {
                DataType.ARMOR -> {
                    val armor = it as UserArmorPiece
                    rarity = armor.armor.armor.rarity
                }
                DataType.WEAPON -> {
                    val weapon = it as UserWeapon
                    rarity = weapon.weapon.weapon.rarity
                }
                DataType.CHARM -> {
                    val charm = it as UserCharm
                    rarity = charm.charm.charm.rarity
                }
                else -> 0
            }
            maxRarity = if (rarity > maxRarity) rarity else maxRarity
        }
        return maxRarity
    }

    private fun calculateSetBonuses(userEquipment: List<UserEquipment>): MutableMap<String, List<ArmorSetBonus>> {
        val setBonuses = mutableMapOf<String, List<ArmorSetBonus>>()
        val setsPresent = userEquipment
                .filter { it.type() == DataType.ARMOR }
                .groupBy { (it as UserArmorPiece).armor.armor.armorset_id }

        //Set Bonus breakpoints are at 2-pieces, 3-pieces, and 4-pieces
        setsPresent.forEach {
            if (it.value.isNotEmpty()) {
                val userArmorPiece = (it.value.first() as UserArmorPiece)
                val activeSetBonuses = userArmorPiece.armor.setBonuses
                        .filter { setBonus -> it.value.size >= setBonus.required }

                if (activeSetBonuses.isNotEmpty()) {
                    setBonuses[activeSetBonuses.first().name!!] = activeSetBonuses
                }
            }
        }

        return setBonuses
    }

    private fun calculateDefenseBase(userEquipment: List<UserEquipment>): Int {
        return userEquipment.sumBy { item ->
            if (item.type() == DataType.ARMOR) {
                (item as UserArmorPiece).armor.armor.defense_base
            } else if (item.type() == DataType.WEAPON) {
                (item as UserWeapon).weapon.weapon.defense
            } else 0
        }
    }

    private fun calculateDefenseMax(userEquipment: List<UserEquipment>): Int {
        return userEquipment.sumBy { item ->
            if (item.type() == DataType.ARMOR) {
                (item as UserArmorPiece).armor.armor.defense_max
            } else if (item.type() == DataType.WEAPON) {
                (item as UserWeapon).weapon.weapon.defense
            } else 0
        }
    }

    private fun calculateDefenseAugMax(userEquipment: List<UserEquipment>): Int {
        return userEquipment.sumBy { item ->
            if (item.type() == DataType.ARMOR) {
                (item as UserArmorPiece).armor.armor.defense_augment_max
            } else if (item.type() == DataType.WEAPON) {
                (item as UserWeapon).weapon.weapon.defense
            } else 0
        }
    }

    private fun calculateFireDefense(userEquipment: List<UserEquipment>): Int {
        return userEquipment.sumBy { item ->
            if (item.type() == DataType.ARMOR) {
                (item as UserArmorPiece).armor.armor.fire
            } else 0
        }
    }

    private fun calculateWaterDefense(userEquipment: List<UserEquipment>): Int {
        return userEquipment.sumBy { item ->
            if (item.type() == DataType.ARMOR) {
                (item as UserArmorPiece).armor.armor.water
            } else 0
        }
    }

    private fun calculateThunderDefense(userEquipment: List<UserEquipment>): Int {
        return userEquipment.sumBy { item ->
            if (item.type() == DataType.ARMOR) {
                (item as UserArmorPiece).armor.armor.thunder
            } else 0
        }
    }

    private fun calculateIceDefense(userEquipment: List<UserEquipment>): Int {
        return userEquipment.sumBy { item ->
            if (item.type() == DataType.ARMOR) {
                (item as UserArmorPiece).armor.armor.ice
            } else 0
        }
    }

    private fun calculateDragonDefense(userEquipment: List<UserEquipment>): Int {
        return userEquipment.sumBy { item ->
            if (item.type() == DataType.ARMOR) {
                (item as UserArmorPiece).armor.armor.dragon
            } else 0
        }
    }

    private fun calculateSkillLevels(userEquipment: List<UserEquipment>, unlockedSkillsFromSetBonuses: Set<Int>): List<SkillLevel> {
        val providedSkills = mutableListOf<SkillLevel>()
        userEquipment.forEach { item ->
            when (item.type()) {
                DataType.ARMOR -> {
                    (item as UserArmorPiece).decorations.forEach { userDecoration ->
                        providedSkills.addAll(userDecoration.decoration.getSkillLevels())
                    }

                    providedSkills.addAll(item.armor.skills)
                }

                DataType.WEAPON -> {
                    (item as UserWeapon).decorations.forEach { userDecoration ->
                        providedSkills.addAll(userDecoration.decoration.getSkillLevels())
                    }

                    providedSkills.addAll(item.weapon.skills)
                }
                DataType.CHARM -> {
                    providedSkills.addAll((item as UserCharm).charm.skills)
                }
                else -> {
                }
            }
        }

        val unlockedSkills = providedSkills.filter { it.skillTree.unlocks_id != null }.map { it.skillTree.unlocks_id }.toMutableSet()
        unlockedSkills.addAll(unlockedSkillsFromSetBonuses)
        val skillLevels = mutableMapOf<Int, SkillLevel>()

        providedSkills.forEach {
            if (skillLevels.containsKey(it.skillTree.id)) {
                val level: Int

                //Determine whether the skill has been unlocked, and set the skill level appropriately
                if (it.skillTree.secret > 0 && unlockedSkills.contains(it.skillTree.id)) {
                    level = addSkillLevels(skillLevels[it.skillTree.id]!!.level, it.level, it.skillTree.max_level)
                } else if (it.skillTree.secret > 0 && !unlockedSkills.contains(it.skillTree.id)) {
                    level = addSkillLevels(skillLevels[it.skillTree.id]!!.level, it.level, it.skillTree.lockedMaxLevel)
                } else {
                    level = addSkillLevels(skillLevels[it.skillTree.id]!!.level, it.level, it.skillTree.max_level)
                }

                val skillLevel = SkillLevel(level)
                skillLevel.skillTree = it.skillTree
                skillLevels[it.skillTree.id] = skillLevel
            } else {
                skillLevels[it.skillTree.id] = it
            }
        }
        val list = skillLevels.map { it.value }.toMutableList()
        list.sortWith(compareByDescending<SkillLevel> { it.level }.thenBy { it.skillTree.id })
        return list
    }

    private fun addSkillLevels(a: Int, b: Int, max: Int): Int {
        return if (a + b > max) max else a + b
    }
}
