package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.adapters.WeaponTypeAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.StandardDivider
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType
import com.gatheringhallstudios.mhworlddatabase.getRouter

/**
 * Fragment used to display the weapon type selection screen.
 * Selecting an option leads to the weapon tree
 */
class WeaponListFragment : RecyclerViewFragment() {
    val adapter = BasicListDelegationAdapter(WeaponTypeAdapterDelegate {
        getRouter().navigateWeaponTree(it)
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter(adapter)

        // Add dividers between items
        recyclerView.addItemDecoration(StandardDivider(DashedDividerDrawable(context!!)))

        // Because localizations for weapon types do not exist in the database and also because MHW
        // will probably not add new weapon types for the life time of the game, title list is generated using string dictionary
        val weaponTypes = listOf(
                WeaponType.GREAT_SWORD,
                WeaponType.LONG_SWORD,
                WeaponType.SWORD_AND_SHIELD,
                WeaponType.DUAL_BLADES,
                WeaponType.HAMMER,
                WeaponType.HUNTING_HORN,
                WeaponType.LANCE,
                WeaponType.GUNLANCE,
                WeaponType.SWITCH_AXE,
                WeaponType.CHARGE_BLADE,
                WeaponType.INSECT_GLAIVE,
                WeaponType.LIGHT_BOWGUN,
                WeaponType.HEAVY_BOWGUN,
                WeaponType.BOW)

        adapter.items = weaponTypes
        adapter.notifyDataSetChanged()
    }
}