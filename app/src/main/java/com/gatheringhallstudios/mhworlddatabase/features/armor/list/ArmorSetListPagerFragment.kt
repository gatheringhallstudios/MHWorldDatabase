package com.gatheringhallstudios.mhworlddatabase.features.armor.list

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.util.pager.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank

class ArmorSetListPagerFragment : BasePagerFragment() {

    override fun onAddTabs(tabs: TabAdder) {
        tabs.addTab(getString(R.string.rank_full_high)) {
            ArmorSetListFragment.newInstance(Rank.HIGH)
        }

        tabs.addTab(getString(R.string.rank_full_low)) {
            ArmorSetListFragment.newInstance(Rank.LOW)
        }
    }
}