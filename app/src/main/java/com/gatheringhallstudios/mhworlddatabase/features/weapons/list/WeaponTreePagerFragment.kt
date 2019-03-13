package com.gatheringhallstudios.mhworlddatabase.features.weapons.list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.data.types.WeaponType
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle

/**
 * Fragment that displays the MHModelTree object.
 * This displays the weapons of a particular weapon type as a tree.
 */
class WeaponTreePagerFragment : BasePagerFragment() {
    companion object {
        const val ARG_WEAPON_TREE_TYPE = "WEAPON_TREE_TYPE"
        const val FILTER_RESULT_CODE = 1
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(WeaponTreeListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAddTabs(tabs: TabAdder) {
        val type = arguments?.getSerializable(ARG_WEAPON_TREE_TYPE) as WeaponType
        setActivityTitle(AssetLoader.getNameFor(type))

        // Load data
        viewModel.setWeaponType(type)

        tabs.addTab(R.string.tab_weapon_normal) {
            WeaponTreeListFragment()
        }

        tabs.addTab(R.string.tab_weapon_kulve) {
            WeaponKulveListFragment()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_weapon_tree, menu)
    }

    /**
     * Handled when a menu item is clicked. True is returned if handled.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                val wtype = viewModel.currentWeaponType
                val state = viewModel.filterState
                val filterFragment = WeaponFilterFragment.newInstance(wtype, state)
                filterFragment.setTargetFragment(this, FILTER_RESULT_CODE)
                filterFragment.show(fragmentManager!!, "Filter")

                true
            }

            // fallback to parent behavior if unhandled
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Receives a dialog result. Currently the only supported dialog is the filter fragment.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != FILTER_RESULT_CODE) {
            return
        }

        val state = data?.getSerializableExtra(WeaponFilterFragment.FILTER_STATE) as? FilterState
        if (state != null) {
            viewModel.filterState = state
        }
    }
}