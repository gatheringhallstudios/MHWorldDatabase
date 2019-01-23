package com.gatheringhallstudios.mhworlddatabase.features.armor.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.data.models.ArmorFull
import com.gatheringhallstudios.mhworlddatabase.features.favorites.FavoritesFeature
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat

/**
 * The main page for displaying armor detail information
 */
class ArmorDetailPagerFragment : BasePagerFragment() {
    private lateinit var viewModel : ArmorDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_favoritable, menu)
        val itemData = viewModel.armor.value
        if (itemData != null && FavoritesFeature.isFavorited(itemData)) {
            menu.findItem(R.id.action_toggle_favorite)
                    .setIcon((context!!.getDrawableCompat(android.R.drawable.btn_star_big_on)))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Try to handle the favorites button onclick here instead of the main activity
        val id = item.itemId
        super.onOptionsItemSelected(item)
        return if (id == R.id.action_toggle_favorite) {
            FavoritesFeature.toggleFavorite(viewModel.armor.value)
            activity!!.invalidateOptionsMenu()
            true
        } else false
    }

    override fun onAddTabs(tabs: BasePagerFragment.TabAdder) {
        // Retrieve MonsterID from args (required!)
        val args = arguments
        val armorId = args!!.getInt(ARG_ARMOR_ID)

        viewModel = ViewModelProviders.of(this).get(ArmorDetailViewModel::class.java)
        viewModel.loadArmor(armorId)

        viewModel.armor.observe(this, Observer<ArmorFull> {
            this.setActivityTitle(it?.armor!!.name)
            //Rerender the menu bar because we are 100% sure we have the item data now
            activity!!.invalidateOptionsMenu()
        })

        tabs.addTab(getString(R.string.tab_armor_detail)) { ArmorDetailFragment() }
        tabs.addTab(getString(R.string.tab_armor_set_summary)) { ArmorSetDetailFragment() }
    }

    companion object {
        const val ARG_ARMOR_ID = "ARMOR"

        @JvmStatic
        fun newInstance(armorId: Int): ArmorDetailPagerFragment {
            val fragment = ArmorDetailPagerFragment()
            fragment.arguments = BundleBuilder()
                    .putInt(ARG_ARMOR_ID, armorId)
                    .build()
            return fragment
        }
    }
}
