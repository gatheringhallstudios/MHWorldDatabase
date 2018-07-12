package com.gatheringhallstudios.mhworlddatabase.features.armor

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorSetView
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import android.support.v4.content.ContextCompat
import com.gatheringhallstudios.mhworlddatabase.components.HeaderItemDecorator


/**
 * Created by Carlos on 3/22/2018.
 */

class ArmorSetListFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ArmorSetListViewModel::class.java)
    }

    val adapter = GroupAdapter<ViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.setAdapter(adapter)

        // TODO Add switching for High and Low rank Armor Set List Fragments
        viewModel.getArmorSetList(Rank.HIGH).observe(this, Observer<List<ArmorSetView>> {
            val items = it?.map {
                val headerItem = ArmorSetHeaderItem(it)
                val bodyItems = it.armor.map { ArmorSetDetailItem(it) }

                return@map ExpandableGroup(headerItem, false).apply {
                    addAll(bodyItems)
                }
            }

            adapter.update(items ?: emptyList())
        })

        // Add dividers between items
        val dividerDrawable = ContextCompat.getDrawable(context!!, R.drawable.listitem_divider)
        val itemDecor = HeaderItemDecorator(dividerDrawable!!)
        recyclerView.addItemDecoration(itemDecor)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title = getString(R.string.armor_title)
    }
}
