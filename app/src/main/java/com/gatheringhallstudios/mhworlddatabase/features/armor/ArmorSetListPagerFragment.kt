package com.gatheringhallstudios.mhworlddatabase.features.armor

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank

class ArmorSetListPagerFragment : BasePagerFragment() {
    override fun onAddTabs(tabs: TabAdder) {
        tabs.addTab(getString(R.string.high_rank_full)) {
            ArmorSetListFragment.newInstance(Rank.HIGH)
        }

        tabs.addTab(getString(R.string.low_rank_full)) {
            ArmorSetListFragment.newInstance(Rank.LOW)
        }
    }
}