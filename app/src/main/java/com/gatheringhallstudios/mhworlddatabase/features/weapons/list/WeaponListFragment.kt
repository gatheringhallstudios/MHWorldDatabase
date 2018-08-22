package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.WeaponTypeAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.StandardDivider
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.WeaponType as WeaponTypeModel
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType as WeaponTypeEnum
import com.gatheringhallstudios.mhworlddatabase.getRouter

class WeaponListFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ViewModel::class.java)
    }

    val adapter = BasicListDelegationAdapter(WeaponTypeAdapterDelegate {
        getRouter().navigateWeaponTree(it.name)
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter(adapter)

        // Add dividers between items
        recyclerView.addItemDecoration(StandardDivider(DashedDividerDrawable(context!!)))

        // Because localizations for weapon types do not exist in the database and also because MHW
        // will probably not add new weapon types for the life time of the game, title list is generated using string dictionary
        val weaponTypes = listOf(
                WeaponTypeModel(getString(R.string.great_sword_title), WeaponTypeEnum.GREAT_SWORD),
                WeaponTypeModel(getString(R.string.long_sword_title), WeaponTypeEnum.LONG_SWORD),
                WeaponTypeModel(getString(R.string.sword_and_shield_title), WeaponTypeEnum.SWORD_AND_SHIELD),
                WeaponTypeModel(getString(R.string.dual_blades_title), WeaponTypeEnum.DUAL_BLADES),
                WeaponTypeModel(getString(R.string.hammer_title), WeaponTypeEnum.HAMMER),
                WeaponTypeModel(getString(R.string.hunting_horn_title), WeaponTypeEnum.HUNTING_HORN),
                WeaponTypeModel(getString(R.string.lance_title), WeaponTypeEnum.LANCE),
                WeaponTypeModel(getString(R.string.gunlance_title), WeaponTypeEnum.GUNLANCE),
                WeaponTypeModel(getString(R.string.switch_axe_title), WeaponTypeEnum.SWITCH_AXE),
                WeaponTypeModel(getString(R.string.charge_blade_title), WeaponTypeEnum.CHARGE_BLADE),
                WeaponTypeModel(getString(R.string.insect_glaive_title), WeaponTypeEnum.INSECT_GLAIVE),
                WeaponTypeModel(getString(R.string.light_bowgun_title), WeaponTypeEnum.LIGHT_BOWGUN),
                WeaponTypeModel(getString(R.string.heavy_bowgun_title), WeaponTypeEnum.HEAVY_BOWGUN),
                WeaponTypeModel(getString(R.string.bow_title), WeaponTypeEnum.HEAVY_BOWGUN))

        adapter.items = weaponTypes
        adapter.notifyDataSetChanged()
    }

    class ViewModel(application: Application) : AndroidViewModel(application) {
        private val dao = MHWDatabase.getDatabase(application).weaponDao()
        //TODO: Check if weapon types and their localizations are meant to be acquired from the database
//        val weaponTypes = dao.loadWeaponTypes(AppSettings.dataLocale)
    }
}