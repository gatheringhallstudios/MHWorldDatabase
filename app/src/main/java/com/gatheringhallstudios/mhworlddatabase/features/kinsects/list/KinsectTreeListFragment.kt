package com.gatheringhallstudios.mhworlddatabase.features.kinsects.list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.util.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.StandardDivider
import com.gatheringhallstudios.mhworlddatabase.features.kinsects.KinsectTreeAdapter
import com.gatheringhallstudios.mhworlddatabase.getRouter


/**
 * Fragment that displays the MHModelTree object.
 * This displays the kinsects of a particular kinsect type as a tree.
 */
class KinsectTreeListFragment : RecyclerViewFragment() {
    companion object {
        const val FILTER_RESULT_CODE = 1
    }

    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(KinsectTreeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.addItemDecoration(StandardDivider(DashedDividerDrawable(context!!)))

        val adapter = KinsectTreeAdapter() {
            getRouter().navigateKinsectDetail(it.id)
        }
        setAdapter(adapter)

        viewModel.nodeListData.observe(this, Observer {
            adapter.setItems(it ?: emptyList())
            adapter.notifyDataSetChanged()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_weapon_tree, menu)
        val filterIcon = menu.findItem(R.id.action_filter)

        viewModel.isFilteredData.observe(this, Observer { isFiltered ->
            filterIcon?.setIcon(when (isFiltered) {
                true -> R.drawable.ic_sys_filter_on
                false -> R.drawable.ic_sys_filter_off
            })
        })
    }

    /**
     * Handled when a menu item is clicked. True is returned if handled.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                val state = viewModel.filterState
                val filterFragment = KinsectFilterFragment.newInstance(state)
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

        val state = data?.getSerializableExtra(KinsectFilterFragment.FILTER_STATE) as? FilterState
        if (state != null) {
            viewModel.filterState = state
        }
    }
}