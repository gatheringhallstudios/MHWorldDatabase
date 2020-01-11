package com.gatheringhallstudios.mhworlddatabase.features.quests.list

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.BasePagerFragment
import com.gatheringhallstudios.mhworlddatabase.data.types.QuestCategory

class QuestListPagerFragment : BasePagerFragment() {
    override fun onAddTabs(tabs: TabAdder) {
        tabs.addTab(R.string.quest_category_assigned) {
            QuestListFragment.newInstance(QuestCategory.ASSIGNED)
        }

        tabs.addTab(R.string.quest_category_optional) {
            QuestListFragment.newInstance(QuestCategory.OPTIONAL)
        }

        tabs.addTab(R.string.quest_category_event) {
            QuestListFragment.newInstance(QuestCategory.EVENT, QuestCategory.SPECIAL)
        }

        tabs.addTab(R.string.quest_category_arena) {
            QuestListFragment.newInstance(QuestCategory.ARENA)
        }
    }
}