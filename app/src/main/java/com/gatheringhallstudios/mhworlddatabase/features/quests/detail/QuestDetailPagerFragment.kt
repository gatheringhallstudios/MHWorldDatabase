package com.gatheringhallstudios.mhworlddatabase.features.quests.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import com.gatheringhallstudios.mhworlddatabase.util.pager.BasePagerFragment

class QuestDetailPagerFragment : BasePagerFragment() {
    companion object {
        const val ARG_QUEST_ID = "QUEST_ID"
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(QuestDetailViewModel::class.java)
    }

    override fun onAddTabs(tabs: TabAdder) {
        val questId = arguments?.getInt(ARG_QUEST_ID) ?: -1

        viewModel.loadQuestData(questId).observe(viewLifecycleOwner, Observer {
            setActivityTitle(it?.name)
        })

        tabs.addTab(R.string.tab_quest_summary) {
            QuestSummaryFragment.newInstance(questId)
        }
        tabs.addTab(R.string.tab_quest_rewards) {
            QuestRewardsFragment.newInstance(questId)
        }
    }
}