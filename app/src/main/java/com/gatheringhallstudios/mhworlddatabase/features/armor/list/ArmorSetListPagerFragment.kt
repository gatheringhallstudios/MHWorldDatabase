package com.gatheringhallstudios.mhworlddatabase.features.armor.list

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.util.applyArguments

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