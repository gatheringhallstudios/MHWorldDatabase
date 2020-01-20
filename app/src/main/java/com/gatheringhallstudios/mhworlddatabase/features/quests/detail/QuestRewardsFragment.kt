package com.gatheringhallstudios.mhworlddatabase.features.quests.detail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.QuestRewardAdapterDelegate
import com.gatheringhallstudios.mhworlddatabase.adapters.common.CategoryAdapter
import com.gatheringhallstudios.mhworlddatabase.components.ChildDivider
import com.gatheringhallstudios.mhworlddatabase.components.DashedDividerDrawable
import com.gatheringhallstudios.mhworlddatabase.features.quests.detail.QuestDetailPagerFragment.Companion.ARG_QUEST_ID
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.util.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.util.applyArguments
import kotlinx.android.synthetic.main.listitem_quest_header.view.*

class QuestRewardsFragment : RecyclerViewFragment() {
    companion object {
        fun newInstance(questId: Int) = QuestRewardsFragment().applyArguments {
            putInt(ARG_QUEST_ID, questId)
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(QuestDetailViewModel::class.java)
    }

    val adapter = CategoryAdapter(QuestRewardAdapterDelegate { reward ->
        getRouter().navigateItemDetail(reward.item.id)
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter(adapter)

        // Add divider
        recyclerView.addItemDecoration(ChildDivider(DashedDividerDrawable(context!!)))

        val questId = arguments?.getInt(ARG_QUEST_ID) ?: -1

        viewModel.loadRewardData(questId).observe(viewLifecycleOwner, Observer {
            adapter.clear()

            val questsGrouped = it.groupBy { it.group }
            for ((group, rewards) in questsGrouped) {
                val groupString = getString(R.string.quest_group, group)
                adapter.addSubSection(groupString, rewards)
            }
        })
    }
}