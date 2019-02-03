package com.gatheringhallstudios.mhworlddatabase.features.items.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.data.models.Item
import com.gatheringhallstudios.mhworlddatabase.features.bookmarks.BookmarksFeature
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat

/**
 * The main page for displaying item detail information
 */
class ItemDetailPagerFragment : BasePagerFragment() {
    private lateinit var viewModel : ItemDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_bookmarkable, menu)
        val itemData = viewModel.item.value
        if (itemData != null && BookmarksFeature.isBookmarked(itemData)) {
            menu.findItem(R.id.action_toggle_bookmark).icon = (context!!.getDrawableCompat(R.drawable.ic_ui_bookmark_on_white))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Try to handle the bookmarks button onclick here instead of the main activity
        val id = item.itemId
        super.onOptionsItemSelected(item)
        return if (id == R.id.action_toggle_bookmark) {
            BookmarksFeature.toggleBookmark(viewModel.item.value)
            activity!!.invalidateOptionsMenu()
            true
        } else false
    }

    override fun onAddTabs(tabs: BasePagerFragment.TabAdder) {
        // Retrieve MonsterID from args (required!)
        val args = arguments
        val itemId = args!!.getInt(ARG_ITEM_ID)

        viewModel = ViewModelProviders.of(this).get(ItemDetailViewModel::class.java)
        viewModel.loadItem(itemId)

        viewModel.item.observe(this, Observer<Item> {
            this.setActivityTitle(it?.name)
            //Rerender the menu bar because we are 100% sure we have the item data now
            activity!!.invalidateOptionsMenu()
        })

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
