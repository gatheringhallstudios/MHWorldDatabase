package com.gatheringhallstudios.mhworlddatabase.features.quests.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.QuestBase
import com.gatheringhallstudios.mhworlddatabase.data.models.QuestReward
import com.gatheringhallstudios.mhworlddatabase.util.tree.MemoizedValue

class QuestDetailViewModel(app: Application): AndroidViewModel(app) {
    private val db = MHWDatabase.getDatabase(app)

    private val questData = MemoizedValue<Int, LiveData<QuestBase>>()
    private val rewardData = MemoizedValue<Int, LiveData<List<QuestReward>>>()

    fun loadQuestData(questId: Int) = questData.get(questId) {
        db.questDao().loadQuest(AppSettings.dataLocale, questId)
    }

    fun loadRewardData(questId: Int) = rewardData.get(questId) {
        db.questDao().loadQuestRewards(AppSettings.dataLocale, questId)
    }
}