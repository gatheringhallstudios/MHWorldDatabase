package com.gatheringhallstudios.mhworlddatabase.features.workshop.selectors

import android.app.Application
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gatheringhallstudios.mhworlddatabase.util.EquipmentFilter
import com.gatheringhallstudios.mhworlddatabase.data.AppDatabase
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.features.workshop.selectors.WorkshopSelectorListFragment.Companion.SelectorMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkshopSelectorViewModel(application: Application) : AndroidViewModel(application) {
    private val appDao = AppDatabase.getAppDataBase(application)!!.userEquipmentSetDao()
    private val charmDao = MHWDatabase.getDatabase(application).charmDao()
    private val weaponDao = MHWDatabase.getDatabase(application).weaponDao()
    private val armorDao = MHWDatabase.getDatabase(application).armorDao()
    private val decorationDao = MHWDatabase.getDatabase(application).decorationDao()
    private val skillDao = MHWDatabase.getDatabase(application).skillDao()

    val armor: MutableLiveData<List<ArmorFull>> = MutableLiveData()
    val weapons: MutableLiveData<List<WeaponFull>> = MutableLiveData()
    val decorations: MutableLiveData<List<Decoration>> = MutableLiveData()
    val charms: MutableLiveData<List<CharmFull>> = MutableLiveData()
    lateinit var skills: LiveData<List<SkillTree>>
    lateinit var listState: Parcelable
    private var armorFilters = EquipmentFilter<ArmorFull>(null)
    private var decorationFilters = EquipmentFilter<Decoration>(null)
    private var charmFilters = EquipmentFilter<CharmFull>(null)
    private var weaponFilters = EquipmentFilter<WeaponFull>(null)

    var filterState: EquipmentFilterState = EquipmentFilterState.default
        set(value) {
            field = value
            isFilterActive.value = !value.isEmpty()

            when (value.selectorMode) {
                SelectorMode.ARMOR -> {
                    armorFilters.clearFilters()

                    if (!value.nameFilter.isNullOrEmpty()) {
                        armorFilters.addFilter(ArmorNameFilter(value.nameFilter!!))
                    }

                    if (!value.elementalDefense.isNullOrEmpty()) {
                        armorFilters.addFilter(ArmorElementalDefenseFilter(value.elementalDefense!!))
                    }

                    if (!value.rank.isNullOrEmpty()) {
                        armorFilters.addFilter(ArmorRankFilter(value.rank!!))
                    }

                    if (!value.skills.isNullOrEmpty()) {
                        armorFilters.addFilter(ArmorSkillsFilter(value.skills!!))
                    }

                    armor.value = armorFilters.renderResults()

                }
                SelectorMode.DECORATION -> {
                    decorationFilters.clearFilters()

                    if (!value.nameFilter.isNullOrEmpty()) {
                        decorationFilters.addFilter(DecorationNameFilter(value.nameFilter!!))
                    }

                    if (!value.slotLevels.isNullOrEmpty()) {
                        decorationFilters.addFilter(DecorationSlotLevelFilter(value.slotLevels!!))
                    }

                    decorations.value = decorationFilters.renderResults()
                }
                SelectorMode.CHARM -> {
                    charmFilters.clearFilters()
                    if (!value.nameFilter.isNullOrEmpty()) {
                        charmFilters.addFilter((CharmNameFilter(value.nameFilter!!)))
                    }

                    if (!value.skills.isNullOrEmpty()) {
                        charmFilters.addFilter(CharmSkillsFilter(value.skills!!))
                    }

                    charms.value = charmFilters.renderResults()
                }
                SelectorMode.WEAPON -> {
                    weaponFilters.clearFilters()

                    if (!value.nameFilter.isNullOrEmpty()) {
                        weaponFilters.addFilter(WeaponNameFilter(value.nameFilter!!))
                    }

                    if (!value.slotLevels.isNullOrEmpty()) {
                        weaponFilters.addFilter(WeaponSlotFilter(value.slotLevels!!))
                    }

                    if (!value.weaponTypes.isNullOrEmpty()) {
                        weaponFilters.addFilter(WeaponTypeFilter(value.weaponTypes!!))
                    }

                    if (!value.elements.isNullOrEmpty()) {
                        weaponFilters.addFilter(WeaponElementFilter(value.elements!!))
                    }

                    if (!value.rank.isNullOrEmpty()) {
                        weaponFilters.addFilter(WeaponRankFilter(value.rank!!))
                    }

                    weapons.value = weaponFilters.renderResults()
                }
            }
        }
    /**
     * Live event that reflects the current is filtered status
     */
    val isFilterActive = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        Log.i("UserEquipmentSet", "CLEARED")
    }

    fun islistStateInitialized(): Boolean {
        return ::listState.isInitialized
    }

    fun loadArmor(langId: String, armorType: ArmorType) {
        GlobalScope.launch(Dispatchers.Main) {
            val armorList = withContext(Dispatchers.IO) {
                armorDao.loadArmorFullByType(langId, armorType)
            }
            armorFilters.equipmentList = armorList
            armor.value = armorFilters.renderResults()
        }
    }

    fun loadCharms(langId: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val charmList = withContext(Dispatchers.IO) {
                charmDao.loadCharmAndSkillList(langId)
            }
            charmFilters.equipmentList = charmList
            charms.value = charmFilters.renderResults()
        }
    }

    fun loadDecorations(langId: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val decoList = withContext(Dispatchers.IO) {
                decorationDao.loadDecorationsWithSkillsSync(langId)
            }
            decorationFilters.equipmentList = decoList
            decorations.value = decorationFilters.renderResults()
        }
    }

    fun loadWeapons(langId: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val weaponList = withContext(Dispatchers.IO) {
                weaponDao.loadWeaponsWithSkillsSync(langId)
            }
            weaponFilters.equipmentList = weaponList
            weapons.value = weaponFilters.renderResults()
        }
    }

    fun loadSkills(langId: String) {
        skills = skillDao.loadSkillTrees(langId);
    }

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
