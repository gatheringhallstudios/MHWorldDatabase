package com.gatheringhallstudios.mhworlddatabase.features.quests.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.components.HeaderItemDivider
import com.gatheringhallstudios.mhworlddatabase.data.types.QuestCategory
import com.gatheringhallstudios.mhworlddatabase.util.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.util.applyArguments
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class QuestListFragment : RecyclerViewFragment() {
    companion object {
        private val ARG_QUEST_CATEGORY = "QUESTLIST_CATEGORY"

        @JvmStatic
        fun newInstance(vararg categories: QuestCategory): QuestListFragment {
            return QuestListFragment().applyArguments {
                putSerializable(ARG_QUEST_CATEGORY, categories)
            }
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(QuestListViewModel::class.java)
    }

    val adapter = GroupAdapter<ViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.setAdapter(adapter)

        // Add dividers between items
        recyclerView.addItemDecoration(HeaderItemDivider(DashedDividerDrawable(requireContext())))

        val categories = arguments?.getSerializable(ARG_QUEST_CATEGORY) as? Array<QuestCategory> ?: emptyArray()
        viewModel.getQuests(categories).observe(viewLifecycleOwner, Observer { quests ->
            adapter.clear()
            quests ?: return@Observer

            // Group by category first (so that if we have multiple categories, last one is last
            val groupings = quests
                    .sortedBy {it.category}.groupBy { it.category }

            for (category in categories) {
                // Sort by star value
                val catQuests = (groupings[category] ?: emptyList()).sortedBy { it.stars_raw }

                // Display all
                for ((stars_raw, catstarQuests) in catQuests.groupBy { it.stars_raw }) {
                    val expandable = ExpandableGroup(QuestListHeaderItem(category, stars_raw), false)
                    expandable.addAll(catstarQuests.map { QuestListDetailItem(it) })
                    adapter.add(expandable)
                }
            }
        })
    }
}