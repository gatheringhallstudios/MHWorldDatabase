package com.gatheringhallstudios.mhworlddatabase.features.quests.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.QuestBase
import com.gatheringhallstudios.mhworlddatabase.data.types.QuestCategory
import com.gatheringhallstudios.mhworlddatabase.util.tree.MemoizedValue

class QuestListViewModel(app: Application) : AndroidViewModel(app) {
    val db = MHWDatabase.getDatabase(app)
    private var questData = MemoizedValue<Set<QuestCategory>, LiveData<List<QuestBase>>>()

    fun getQuests(categories: Array<QuestCategory>): LiveData<List<QuestBase>> {
        return questData.get(setOf(*categories)) {
            db.questDao().loadQuests(AppSettings.dataLocale, categories)
        }
    }
}