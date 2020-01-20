package com.gatheringhallstudios.mhworlddatabase.features.quests.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.Location
import com.gatheringhallstudios.mhworlddatabase.data.models.QuestBase
import com.gatheringhallstudios.mhworlddatabase.data.models.QuestMonster
import com.gatheringhallstudios.mhworlddatabase.data.models.QuestReward
import com.gatheringhallstudios.mhworlddatabase.util.tree.LiveMemoizedValue
import com.gatheringhallstudios.mhworlddatabase.util.tree.MemoizedValue

class QuestDetailViewModel(app: Application): AndroidViewModel(app) {
    private val db = MHWDatabase.getDatabase(app)

    private val questData = LiveMemoizedValue<Int, QuestBase>()
    private val locationData = LiveMemoizedValue<Int, Location>()
    private val questMonsterData = LiveMemoizedValue<Int, List<QuestMonster>>()
    private val rewardData = LiveMemoizedValue<Int, List<QuestReward>>()

    fun loadQuestData(questId: Int) = questData.get(questId) {
        db.questDao().loadQuest(AppSettings.dataLocale, questId)
    }

    fun loadQuestLocation(questId: Int) = locationData.get(questId) {
        Transformations.switchMap(loadQuestData(questId)) {
            db.locationDao().loadLocation(AppSettings.dataLocale, it.location_id)
        }
    }

    fun loadQuestMonsters(questId: Int) = questMonsterData.get(questId) {
        db.questDao().loadQuestMonsters(AppSettings.dataLocale, questId)
    }

    fun loadRewardData(questId: Int) = rewardData.get(questId) {
        db.questDao().loadQuestRewards(AppSettings.dataLocale, questId)
    }
}