package com.gatheringhallstudios.mhworlddatabase.features.monsters

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.features.monsters.list.MonsterListFragment
import com.gatheringhallstudios.mhworlddatabase.features.monsters.list.MonsterListViewModel

import butterknife.BindString
import butterknife.BindView

/**
 * The main screen page for a monster list. Contains MonstListFragments as tabs
 * Monster Hub
 */

class MonsterListPagerFragment : BasePagerFragment() {
    /*
     * TODO Add any scroll or pager state to a MonsterListPagerViewModel to better support rotation and backstack
     */

    override fun onAddTabs(tabs: BasePagerFragment.TabAdder) {
        tabs.addTab(getString(R.string.monsters_list_tab_large)) {
            MonsterListFragment.newInstance(MonsterListViewModel.Tab.LARGE)
        }

        tabs.addTab(getString(R.string.monsters_list_tab_small)) {
            MonsterListFragment.newInstance(MonsterListViewModel.Tab.SMALL)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title = getString(R.string.monsters_list_title)
    }
}
