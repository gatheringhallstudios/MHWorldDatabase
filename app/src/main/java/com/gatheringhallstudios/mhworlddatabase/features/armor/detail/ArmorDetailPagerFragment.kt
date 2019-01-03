package com.gatheringhallstudios.mhworlddatabase.features.armor.detail

import androidx.lifecycle.ViewModelProviders

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder

/**
 * The main page for displaying item detail information
 */
class ArmorDetailPagerFragment : BasePagerFragment() {

    override fun onAddTabs(tabs: BasePagerFragment.TabAdder) {
        // Retrieve MonsterID from args (required!)
        val args = arguments
        val armorId = args!!.getInt(ARG_ARMOR_ID)

        val viewModel = ViewModelProviders.of(this).get(ArmorDetailViewModel::class.java)
        viewModel.loadArmor(armorId)

        tabs.addTab(getString(R.string.tab_armor_detail)) { ArmorDetailFragment() }
        tabs.addTab(getString(R.string.tab_armor_set_summary)) { ArmorSetDetailFragment() }
    }

    companion object {
        const val ARG_ARMOR_ID = "ARMOR"

        @JvmStatic
        fun newInstance(armorId: Int): ArmorDetailPagerFragment {
            val fragment = ArmorDetailPagerFragment()
            fragment.arguments = BundleBuilder()
                    .putSerializable(ARG_ARMOR_ID, armorId)
                    .build()
            return fragment
        }
    }
}
