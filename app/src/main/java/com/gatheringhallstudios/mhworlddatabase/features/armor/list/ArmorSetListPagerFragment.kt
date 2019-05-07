package com.gatheringhallstudios.mhworlddatabase.features.armor.list

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.util.applyArguments

class ArmorSetListPagerFragment : BasePagerFragment() {
    companion object {
        const val ARG_MODE = "ARMORLIST_MODE"
        const val ARG_SET_ID = "ARMORLIST_LIST_ID" //The equipment set that is currently being handled when in builder mode
        const val ARG_PREV_ID = "ARMORLIST_PREV_ID"  //What ID the new selection will be replacing when in builder mode
        const val ARG_ITEM_FILTER = "ARMORLIST_ITEM_FILTER" //What class armor to limit the selector to

        @JvmStatic
        fun newInstance(mode: ArmorSetListPagerFragment.ArmorSetListMode = ArmorSetListMode.LIST, prevId: Int): ArmorSetListPagerFragment {
            return ArmorSetListPagerFragment().applyArguments {
                putSerializable(ARG_MODE, mode)
                putInt(ARG_PREV_ID, prevId)
            }
        }
    }
    //Enum for indicating if this fragment is for showing the armor list feature or is being used to select a
    //piece for the equipment set builder
    enum class ArmorSetListMode {
        LIST,
        BUILDER
    }

    override fun onAddTabs(tabs: TabAdder) {
        val mode = arguments?.getSerializable(ArmorSetListFragment.ARG_MODE) as? ArmorSetListPagerFragment.ArmorSetListMode
        val setId = arguments?.getInt(ArmorSetListFragment.ARG_SET_ID)
        val prevId = arguments?.getInt(ArmorSetListFragment.ARG_PREV_ID)
        val filter = arguments?.getSerializable(ArmorSetListFragment.ARG_ITEM_FILTER) as? ArmorType

        tabs.addTab(getString(R.string.rank_full_high)) {
            ArmorSetListFragment.newInstance(Rank.HIGH, mode, setId, prevId, filter)
        }

        tabs.addTab(getString(R.string.rank_full_low)) {
            ArmorSetListFragment.newInstance(Rank.LOW, mode, setId, prevId, filter)
        }
    }
}