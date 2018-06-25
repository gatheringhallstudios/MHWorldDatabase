package com.gatheringhallstudios.mhworlddatabase.features.monsters.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.MonsterDao
import com.gatheringhallstudios.mhworlddatabase.data.types.MonsterSize
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView

/**
 * A viewmodel for any monster list fragment
 * Created by Carlos on 3/4/2018.
 */
class MonsterListViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).monsterDao()

    enum class Tab {
        LARGE,
        SMALL
    }

    private var currentTab: Tab? = null
    lateinit var monsters: LiveData<List<MonsterView>>

    fun setTab(tab: Tab) {
        if (currentTab == tab) {
            return
        }

        currentTab = tab

        val monsterSize = when (tab) {
            MonsterListViewModel.Tab.LARGE -> MonsterSize.LARGE
            MonsterListViewModel.Tab.SMALL -> MonsterSize.SMALL
        }

        monsters = dao.loadList(AppSettings.dataLocale, monsterSize)
    }
}
