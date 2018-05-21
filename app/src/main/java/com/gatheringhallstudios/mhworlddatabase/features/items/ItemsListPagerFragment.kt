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

    private val TAG = javaClass.simpleName

    @BindView(R.id.tab_layout)
    internal var tabLayout: TabLayout? = null
    @BindView(R.id.pager_list)
    internal var viewPager: ViewPager? = null

    @BindString(R.string.items_tab_item)
    internal var tabTitleItem: String? = null
    @BindString(R.string.items_tab_materials)
    internal var tabTitleMaterials: String? = null
    @BindString(R.string.items_tab_ammo)
    internal var tabTitleAmmo: String? = null
    @BindString(R.string.items_tab_account)
    internal var tabTitleAccount: String? = null

    override fun onAddTabs(tabs: BasePagerFragment.TabAdder) {
        tabs.addTab(tabTitleItem
        ) { ItemListFragment.newInstance(ItemCategory.ITEM) }

        tabs.addTab(tabTitleMaterials
        ) { ItemListFragment.newInstance(ItemCategory.MATERIAL) }
        tabs.addTab(tabTitleAmmo
        ) { ItemListFragment.newInstance(ItemCategory.AMMO) }

        tabs.addTab(tabTitleAccount
        ) { ItemListFragment.newInstance(ItemCategory.ACCOUNT) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity!!.title = getString(R.string.items_title)
    }
}
