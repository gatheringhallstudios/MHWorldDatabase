package com.gatheringhallstudios.mhworlddatabase.features.items

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.features.items.detail.ItemDetailViewModel
import com.gatheringhallstudios.mhworlddatabase.features.items.detail.ItemLocationsFragment
import com.gatheringhallstudios.mhworlddatabase.features.items.detail.ItemSummaryFragment
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

        tabs.addTab(getString(R.string.item_tab_summary)) { ItemSummaryFragment() }
        tabs.addTab(getString(R.string.item_tab_locations)) { ItemLocationsFragment() }
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
