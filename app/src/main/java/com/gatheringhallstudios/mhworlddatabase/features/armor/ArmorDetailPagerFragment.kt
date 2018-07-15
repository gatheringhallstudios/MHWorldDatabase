package com.gatheringhallstudios.mhworlddatabase.features.armor

import android.arch.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment

class ArmorDetailPagerFragment : BasePagerFragment() {
    companion object {
        const val ARG_ARMOR_ID = "ARMOR"
    }

    override fun onAddTabs(tabs: TabAdder) {
        // Retrieve MonsterID from args (required!)
        val args = arguments
        val armorId = args!!.getInt(ARG_ARMOR_ID)

        val viewModel = ViewModelProviders.of(this).get(ArmorDetailViewModel::class.java)
        viewModel.loadArmor(armorId)

        tabs.addTab(getString(R.string.armor_detail_tab_summary)) { ArmorSummaryFragment() }
        tabs.addTab(getString(R.string.armor_detail_tab_components)) { ArmorComponentListFragment() }
    }
}