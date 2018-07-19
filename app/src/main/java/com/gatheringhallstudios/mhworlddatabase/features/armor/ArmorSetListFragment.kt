package com.gatheringhallstudios.mhworlddatabase.features.armor

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.applyArguments
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.HeaderItemDecorator
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.models.ArmorSet
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder


/**
 * Created by Carlos on 3/22/2018.
 */

class ArmorSetListFragment : RecyclerViewFragment() {
    companion object {
        const val ARG_RANK = "ARMORLIST_RANK"

        @JvmStatic
        fun newInstance(rank: Rank): ArmorSetListFragment {
            return ArmorSetListFragment().applyArguments {
                putSerializable(ARG_RANK, rank)
            }
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ArmorSetListViewModel::class.java)
    }

    val adapter = GroupAdapter<ViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.setAdapter(adapter)

        val rank = arguments?.getSerializable(ARG_RANK) as? Rank

        viewModel.getArmorSetList(rank).observe(this, Observer<List<ArmorSet>> {
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
