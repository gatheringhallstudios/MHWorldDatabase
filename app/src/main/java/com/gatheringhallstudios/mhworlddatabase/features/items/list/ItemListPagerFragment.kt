package com.gatheringhallstudios.mhworlddatabase.features.items.list

import android.os.Bundle

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.data.types.ItemCategory

/**
 * Monster Hub
 */

class ItemListPagerFragment : BasePagerFragment() {

    override fun onAddTabs(tabs: BasePagerFragment.TabAdder) {
        tabs.addTab(getString(R.string.items_tab_item)) {
            ItemListFragment.newInstance(ItemCategory.ITEM)
        }

        tabs.addTab(getString(R.string.items_tab_materials)) {
            ItemListFragment.newInstance(ItemCategory.MATERIAL)
        }

        tabs.addTab(getString(R.string.items_tab_ammo)) {
            ItemListFragment.newInstance(ItemCategory.AMMO)
        }

        tabs.addTab(getString(R.string.items_tab_misc)) {
            ItemListFragment.newInstance(ItemCategory.MISC)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.title = getString(R.string.items_title)
    }
}
