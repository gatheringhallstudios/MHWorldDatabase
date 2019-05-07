package com.gatheringhallstudios.mhworlddatabase.features.charms.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.common.MHModelTreeFilter
import com.gatheringhallstudios.mhworlddatabase.data.AppDatabase
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.Armor
import com.gatheringhallstudios.mhworlddatabase.data.models.Charm
import com.gatheringhallstudios.mhworlddatabase.data.models.MHModelTree
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import kotlinx.coroutines.*

/**
 * Viewmodel for the charm list.
 * Loaded synchronously to
 */
class CharmListViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).charmDao()
    private val userEquipmentDao = AppDatabase.getAppDataBase(application)!!.userEquipmentSetDao()

    /**
     * Encapsulates the charm tree and performs filtering on it
     */
    private val filter = MHModelTreeFilter<Charm>()

    /**
     * Transport of result data to the view.
     */
    val charmData = MutableLiveData<List<Charm>>()

    /**
     * Returns the current filter state for final only
     */
    val isFinal get() = filter.finalOnly

    init {
        GlobalScope.launch(Dispatchers.Main) {
            val charmList = withContext(Dispatchers.IO) {
                dao.loadCharmsSync(AppSettings.dataLocale)
            }

            filter.tree = MHModelTree(charmList)
            updateCharmData()
        }
    }

    fun setShowFinal(final: Boolean) {
        filter.finalOnly = final
        updateCharmData()
    }

    /**
     * Internal helper called to update the charm data sent back to the client
     */
    private fun updateCharmData() {
        charmData.value = filter.renderResults().map { it.value }
    }

    fun updateCharmForArmorSet(newArmor: Charm, userEquipmentSetId: Int, prevId: Int?) {
        if (prevId != null) {
            userEquipmentDao.deleteUserEquipmentEquipment(prevId, DataType.CHARM, userEquipmentSetId)
        }

        userEquipmentDao.createUserEquipmentEquipment(newArmor.id, DataType.CHARM, userEquipmentSetId)
    }
}