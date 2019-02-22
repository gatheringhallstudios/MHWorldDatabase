package com.gatheringhallstudios.mhworlddatabase.features.weapons.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeaponFamilyData(
        val familyPath: List<Weapon>,
        val finalWeapons: List<Weapon>
)

/**
 * Viewmodel used for weapon detail data
 */
class WeaponDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).weaponDao()

    var weaponId: Int = -1
        private set

    val weaponData = MutableLiveData<WeaponFull>()
    val weaponFamilyData = MutableLiveData<WeaponFamilyData>()

    /**
     * The current value of the stored weapon data.
     * Alias for weaponData.value
     */
    val weapon: WeaponFull? get() = weaponData.value

    /**
     * Sets the weapon id to be loaded.
     */
    fun loadWeapon(weaponId: Int) {
        if (this.weaponId == weaponId) return

        this.weaponId = weaponId

        GlobalScope.launch(Dispatchers.Main) {
            val langId = AppSettings.dataLocale

            val weapon = withContext(Dispatchers.IO) {
                dao.loadWeaponFullSync(langId, weaponId)
            }

            val node = withContext(Dispatchers.IO) {
                val trees = dao.loadWeaponTrees(langId, weapon.weapon.weapon_type)
                trees.getModel(weaponId)
            }

            // Update values if still relevant
            if (node != null && weapon.entityId == weaponId) {
                weaponData.value = weapon
                weaponFamilyData.value = WeaponFamilyData(
                        familyPath = node.path.map { it.value },
                        finalWeapons = node.leaves.map { it.value }
                )
            }
        }
    }
}