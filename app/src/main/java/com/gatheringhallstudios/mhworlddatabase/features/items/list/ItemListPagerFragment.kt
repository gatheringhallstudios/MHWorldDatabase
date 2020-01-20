package com.gatheringhallstudios.mhworlddatabase.features.items.list

import android.os.Bundle

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.util.pager.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.data.types.ItemCategory

/**
 * Monster Hub
 */

class ItemListPagerFragment : BasePagerFragment() {

    override fun onAddTabs(tabs: BasePagerFragment.TabAdder) {
        tabs.addTab(getString(R.string.tab_item_list_items)) {
            ItemListFragment.newInstance(ItemCategory.ITEM)
        }

        tabs.addTab(getString(R.string.tab_item_list_materials)) {
            ItemListFragment.newInstance(ItemCategory.MATERIAL)
        }

        tabs.addTab(getString(R.string.tab_item_list_ammo)) {
            ItemListFragment.newInstance(ItemCategory.AMMO)
        }

        tabs.addTab(getString(R.string.tab_item_list_misc)) {
            ItemListFragment.newInstance(ItemCategory.MISC)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.title = getString(R.string.title_item_list)
    }
}
