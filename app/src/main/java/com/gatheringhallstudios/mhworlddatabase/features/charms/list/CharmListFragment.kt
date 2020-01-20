package com.gatheringhallstudios.mhworlddatabase.features.charms.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.CharmAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.util.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.StandardDivider
import com.gatheringhallstudios.mhworlddatabase.getRouter

class CharmListFragment : RecyclerViewFragment() {
    companion object {
        const val ARG_MODE = "CHARMLIST_MODE"
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CharmListViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val adapter = BasicListDelegationAdapter(CharmAdapterDelegate {
            getRouter().navigateCharmDetail(it.id)
        })
        setAdapter(adapter)

        // Add dividers between items
        recyclerView.addItemDecoration(StandardDivider(DashedDividerDrawable(context!!)))

        viewModel.charmData.observe(this, Observer {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_charm_tree, menu)
        menu.findItem(R.id.final_toggle).isChecked = viewModel.isFinal
    }

    /**
     * Handled when a menu item is clicked. True is returned if handled.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.final_toggle -> {
                val newSetting = !item.isChecked
                viewModel.setShowFinal(newSetting)
                item.isChecked = newSetting
                true
            }

            // fallback to parent behavior if unhandled
            else -> super.onOptionsItemSelected(item)
        }
    }
}