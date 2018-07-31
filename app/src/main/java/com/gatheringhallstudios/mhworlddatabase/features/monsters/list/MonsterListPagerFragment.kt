package com.gatheringhallstudios.mhworlddatabase.features.monsters.list

import android.os.Bundle

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.data.types.MonsterSize

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
            MonsterListFragment.newInstance(MonsterSize.LARGE)
        }

        tabs.addTab(getString(R.string.monsters_list_tab_small)) {
            MonsterListFragment.newInstance(MonsterSize.SMALL)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title = getString(R.string.monsters_list_title)
    }
}
