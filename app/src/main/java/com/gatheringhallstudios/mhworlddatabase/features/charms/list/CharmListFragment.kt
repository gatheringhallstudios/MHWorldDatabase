package com.gatheringhallstudios.mhworlddatabase.features.charms.list

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.CharmAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.StandardDivider
import com.gatheringhallstudios.mhworlddatabase.getRouter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharmListFragment : RecyclerViewFragment() {
    companion object {
        const val ARG_MODE = "CHARMLIST_MODE"
        const val ARG_SET_ID = "CHARMLIST_LIST_ID" //The equipment set that is currently being handled when in builder mode
        const val ARG_PREV_ID = "CHARMLIST_PREV_ID" //What ID the new selection will be replacing when in builder mode

        //Enum for indicating if this fragment is for showing the armor list feature or is being used to select a
        //piece for the equipment set builder
        enum class CharmListMode {
            LIST,
            BUILDER
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CharmListViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mode = arguments?.getSerializable(ARG_MODE) as? CharmListMode
        val setId = arguments?.getInt(ARG_SET_ID)
        val prevId = arguments?.getInt(ARG_PREV_ID)

        val adapter = BasicListDelegationAdapter(CharmAdapterDelegate {
            if (mode == Companion.CharmListMode.BUILDER) {
                GlobalScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO) {
                        viewModel.updateCharmForArmorSet(it, setId!!, prevId)
                    }

                    getRouter().goBack()
                }
            } else {
                getRouter().navigateCharmDetail(it.id)
            }
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