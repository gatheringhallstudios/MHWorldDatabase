package com.gatheringhallstudios.mhworlddatabase.features.items.detail

import androidx.lifecycle.ViewModelProviders

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder

/**
 * The main page for displaying item detail information
 */
class ItemDetailPagerFragment : BasePagerFragment() {

    override fun onAddTabs(tabs: BasePagerFragment.TabAdder) {
        // Retrieve MonsterID from args (required!)
        val args = arguments
        val itemId = args!!.getInt(ARG_ITEM_ID)

        val viewModel = ViewModelProviders.of(this).get(ItemDetailViewModel::class.java)
        viewModel.loadItem(itemId)

        tabs.addTab(getString(R.string.tab_item_detail_summary)) { ItemSummaryFragment() }
        tabs.addTab(getString(R.string.tab_item_detail_usage)) { ItemUsageFragment() }
        tabs.addTab(getString(R.string.tab_item_detail_acquisition)) { ItemAcquisitionFragment() }
    }

    companion object {
        val ARG_ITEM_ID = "ITEM_ID"

        @JvmStatic
        fun newInstance(itemId: Int): ItemDetailPagerFragment {
            val fragment = ItemDetailPagerFragment()
            fragment.arguments = BundleBuilder()
                    .putSerializable(ARG_ITEM_ID, itemId)
                    .build()
            return fragment
        }
    }
}
