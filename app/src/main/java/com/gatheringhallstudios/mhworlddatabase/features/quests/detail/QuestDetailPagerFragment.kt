package com.gatheringhallstudios.mhworlddatabase.features.quests.detail

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.util.pager.BasePagerFragment

class QuestDetailPagerFragment : BasePagerFragment() {
    companion object {
        const val ARG_QUEST_ID = "QUEST_ID"
    }

    override fun onAddTabs(tabs: TabAdder) {
        val questId = arguments?.getInt(ARG_QUEST_ID) ?: -1
        tabs.addTab(R.string.tab_quest_summary) {
            QuestSummaryFragment.newInstance(questId)
        }
        tabs.addTab(R.string.tab_quest_rewards) {
            QuestRewardsFragment.newInstance(questId)
        }
    }
}