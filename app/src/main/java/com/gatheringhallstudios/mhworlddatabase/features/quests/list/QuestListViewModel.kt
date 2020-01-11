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

class QuestListViewModel(app: Application) : AndroidViewModel(app) {
    val db = MHWDatabase.getDatabase(app)
    private var questData: LiveData<List<QuestBase>>? = null
    private val selectedCategories = mutableSetOf<String>()

    fun setCategories(categories: Array<QuestCategory>): LiveData<List<QuestBase>> {
        val newCategories = mutableSetOf<String>()
        if (newCategories == selectedCategories && questData != null) {
            return questData!!
        }

        selectedCategories.clear()
        selectedCategories.addAll(newCategories)
        val newData = db.questDao().getQuests(AppSettings.dataLocale, categories)
        questData = newData
        return newData
    }
}