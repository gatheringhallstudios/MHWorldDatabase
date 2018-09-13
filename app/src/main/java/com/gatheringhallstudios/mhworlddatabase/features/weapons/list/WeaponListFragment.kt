package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.WeaponTypeAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.StandardDivider
import com.gatheringhallstudios.mhworlddatabase.data.models.WeaponType as WeaponTypeModel
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType as WeaponTypeEnum
import com.gatheringhallstudios.mhworlddatabase.getRouter

class WeaponListFragment : RecyclerViewFragment() {
    val adapter = BasicListDelegationAdapter(WeaponTypeAdapterDelegate {
        getRouter().navigateWeaponTree(it.name, it.weapon_type)
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter(adapter)

        // Add dividers between items
        recyclerView.addItemDecoration(StandardDivider(DashedDividerDrawable(context!!)))

        // Because localizations for weapon types do not exist in the database and also because MHW
        // will probably not add new weapon types for the life time of the game, title list is generated using string dictionary
        val weaponTypes = listOf(
                WeaponTypeModel(getString(R.string.title_great_sword), WeaponTypeEnum.GREAT_SWORD),
                WeaponTypeModel(getString(R.string.title_long_sword), WeaponTypeEnum.LONG_SWORD),
                WeaponTypeModel(getString(R.string.title_sword_and_shield), WeaponTypeEnum.SWORD_AND_SHIELD),
                WeaponTypeModel(getString(R.string.title_dual_blades), WeaponTypeEnum.DUAL_BLADES),
                WeaponTypeModel(getString(R.string.title_hammer), WeaponTypeEnum.HAMMER),
                WeaponTypeModel(getString(R.string.title_hunting_horn), WeaponTypeEnum.HUNTING_HORN),
                WeaponTypeModel(getString(R.string.title_lance), WeaponTypeEnum.LANCE),
                WeaponTypeModel(getString(R.string.title_gunlance), WeaponTypeEnum.GUNLANCE),
                WeaponTypeModel(getString(R.string.title_switch_axe), WeaponTypeEnum.SWITCH_AXE),
                WeaponTypeModel(getString(R.string.title_charge_blade), WeaponTypeEnum.CHARGE_BLADE),
                WeaponTypeModel(getString(R.string.title_insect_glaive), WeaponTypeEnum.INSECT_GLAIVE),
                WeaponTypeModel(getString(R.string.title_light_bowgun), WeaponTypeEnum.LIGHT_BOWGUN),
                WeaponTypeModel(getString(R.string.title_heavy_bowgun), WeaponTypeEnum.HEAVY_BOWGUN),
                WeaponTypeModel(getString(R.string.title_bow), WeaponTypeEnum.HEAVY_BOWGUN))

        adapter.items = weaponTypes
        adapter.notifyDataSetChanged()
    }
}