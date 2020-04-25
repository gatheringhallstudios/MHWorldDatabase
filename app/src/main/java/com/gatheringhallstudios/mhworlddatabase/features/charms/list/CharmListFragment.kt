package com.gatheringhallstudios.mhworlddatabase.features.charms.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.StandardDivider
import com.gatheringhallstudios.mhworlddatabase.data.models.Charm
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.RecyclerViewFragment
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

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
        val adapter = GroupAdapter<ViewHolder>()
        setAdapter(adapter)

        // Add dividers between items
        recyclerView.addItemDecoration(StandardDivider(DashedDividerDrawable(context!!)))

        if (adapter.itemCount == 0) {
            viewModel.charmData.observe(this, Observer<List<Charm>> {
                //Group up charms by type (name)
                val groups = it?.groupBy {
                    val targetIndex = it.name!!.indexOfLast { itr ->
                        //For NA and European languages
                        itr != 'I' && itr != 'V' &&
                                //For Russian
                                !itr.isDigit() && !itr.isWhitespace() &&
                                //For Asian languages (unicode roman numerals instead of ascii)
                                itr.toInt() !in 8544..8579
                    }
                    it.name.substring(0, targetIndex + 1)
                }

                val items = groups?.map { itr ->
                    val headerItem = CharmHeaderItem(
                            Charm(itr.value[0].id,
                                    itr.key,
                                    itr.value[0].rarity,
                                    itr.value[0].previous_id))
                    val bodyItems = itr.value.map {
                        CharmDetailItem(it) {
                            getRouter().navigateCharmDetail(it.id)
                        }
                    }

                    return@map ExpandableGroup(headerItem, false).apply {
                        addAll(bodyItems)
                    }
                }

                adapter.update(items ?: emptyList())
            })
        }
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