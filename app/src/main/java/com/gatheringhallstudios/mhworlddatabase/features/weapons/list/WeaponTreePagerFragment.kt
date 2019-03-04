package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle

/**
 * Fragment that displays the MHModelTree object.
 * This displays the weapons of a particular weapon type as a tree.
 */
class WeaponTreePagerFragment : BasePagerFragment() {
    companion object {
        const val ARG_WEAPON_TREE_TYPE = "WEAPON_TREE_TYPE"
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(WeaponTreeListViewModel::class.java)
    }

    override fun onAddTabs(tabs: TabAdder) {
        val type = arguments?.getSerializable(ARG_WEAPON_TREE_TYPE) as WeaponType
        setActivityTitle(AssetLoader.getNameFor(type))

        // Load data
        viewModel.setWeaponType(type)

        tabs.addTab(R.string.tab_weapon_normal) {
            WeaponTreeListFragment()
        }

        tabs.addTab(R.string.tab_weapon_kulve) {
            WeaponKulveListFragment()
        }
    }
}