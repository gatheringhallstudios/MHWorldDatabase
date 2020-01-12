package com.gatheringhallstudios.mhworlddatabase.features.quests.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.QuestBase
import com.gatheringhallstudios.mhworlddatabase.data.types.QuestCategory
import com.gatheringhallstudios.mhworlddatabase.util.tree.MemoizedValue

class QuestListViewModel(app: Application) : AndroidViewModel(app) {
    val db = MHWDatabase.getDatabase(app)
    private var questData = MemoizedValue<Set<QuestCategory>, LiveData<List<QuestBase>>>()

    fun setCategories(categories: Array<QuestCategory>): LiveData<List<QuestBase>> {
        return questData.get(setOf(*categories)) {
            db.questDao().getQuests(AppSettings.dataLocale, categories)
        }
    }
}