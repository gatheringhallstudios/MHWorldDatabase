package com.gatheringhallstudios.mhworlddatabase.features.weapons.detail

import androidx.lifecycle.ViewModelProvider
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.util.pager.BasePagerFragment

/**
 * Fragment for the Weapon detail screen. Contains both the detail and tree tab.
 */
class WeaponDetailPagerFragment: BasePagerFragment() {
    companion object {
        const val ARG_WEAPON_ID = "WEAPON"
    }

    /**
     * Returns the viewmodel owned by this parent fragment
     */
    private val viewModel: WeaponDetailViewModel by lazy {
        ViewModelProvider(this).get(WeaponDetailViewModel::class.java)
    }

    override fun onAddTabs(tabs: TabAdder) {
        val weaponId = arguments?.getInt(ARG_WEAPON_ID) ?: -1
        viewModel.loadWeapon(weaponId)

        tabs.addTab(R.string.tab_weapon_detail) { WeaponDetailFragment() }
        tabs.addTab(R.string.tab_weapon_tree) { WeaponDetailFamilyFragment() }
    }
}
