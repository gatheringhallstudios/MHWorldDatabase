package com.gatheringhallstudios.mhworlddatabase.features.items

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.data.types.ItemCategory
import com.gatheringhallstudios.mhworlddatabase.features.monsters.list.MonsterListFragment
import com.gatheringhallstudios.mhworlddatabase.features.monsters.list.MonsterListViewModel

import butterknife.BindString
import butterknife.BindView

/**
 * Monster Hub
 */

class ItemsListPagerFragment : BasePagerFragment() {

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

        tabs.addTab(getString(R.string.items_tab_account)) {
            ItemListFragment.newInstance(ItemCategory.ACCOUNT)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.title = getString(R.string.items_title)
    }
}
