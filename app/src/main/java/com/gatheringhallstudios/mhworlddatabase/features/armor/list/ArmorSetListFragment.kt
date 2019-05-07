package com.gatheringhallstudios.mhworlddatabase.features.armor.list

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.util.applyArguments
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.HeaderItemDivider
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.models.ArmorSet
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Created by Carlos on 3/22/2018.
 */

class ArmorSetListFragment : RecyclerViewFragment() {
    companion object {
        const val ARG_RANK = "ARMORLIST_RANK"
        const val ARG_MODE = "ARMORLIST_MODE"
        const val ARG_SET_ID = "ARMORLIST_LIST_ID" //The equipment set that is currently being handled when in builder mode
        const val ARG_PREV_ID = "ARMORLIST_PREV_ID" //What ID the new selection will be replacing when in builder mode
        const val ARG_ITEM_FILTER = "ARMORLIST_ITEM_FILTER" //What class armor to limit the selector to

        @JvmStatic
        fun newInstance(rank: Rank, mode: ArmorSetListPagerFragment.ArmorSetListMode? = ArmorSetListPagerFragment.ArmorSetListMode.LIST, userArmorSetId: Int?, prevId: Int?, filter: ArmorType?): ArmorSetListFragment {
            return ArmorSetListFragment().applyArguments {
                putSerializable(ARG_RANK, rank)
                putSerializable(ARG_MODE, mode)
                if (userArmorSetId != null) putInt(ARG_SET_ID, userArmorSetId)
                if (prevId != null) putInt(ARG_PREV_ID, prevId)
                if (filter != null) putSerializable(ARG_ITEM_FILTER, filter)
            }
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ArmorSetListViewModel::class.java)
    }

    val adapter = GroupAdapter<ViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.setAdapter(adapter)

        // Add dividers between items
        recyclerView.addItemDecoration(HeaderItemDivider(DashedDividerDrawable(context!!)))

        if (adapter.itemCount == 0) {
            val rank = arguments?.getSerializable(ARG_RANK) as? Rank
            val mode = arguments?.getSerializable(ARG_MODE) as? ArmorSetListPagerFragment.ArmorSetListMode
            val setId = arguments?.getInt(ARG_SET_ID)
            val prevId = arguments?.getInt(ARG_PREV_ID)
            val filter = arguments?.getSerializable(ArmorSetListFragment.ARG_ITEM_FILTER) as? ArmorType

            viewModel.getArmorSetList(rank).observe(this, Observer<List<ArmorSet>> {
                val items = it?.map {
                    val headerItem = ArmorSetHeaderItem(it)
                    val bodyItems = it.armor.filter { if (filter != null) it.armor_type == filter else true }
                            .map {
                                ArmorSetDetailItem(it) {
                                    if (mode == ArmorSetListPagerFragment.ArmorSetListMode.BUILDER) {
                                        GlobalScope.launch(Dispatchers.Main) {
                                            withContext(Dispatchers.IO) {
                                                viewModel.updateArmorPieceForArmorSet(it, setId!!, prevId)
                                            }

                                            getRouter().goBack()
                                        }
                                    } else {
                                        getRouter().navigateArmorDetail(it.id)
                                    }
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title = getString(R.string.title_armor)
    }
}
