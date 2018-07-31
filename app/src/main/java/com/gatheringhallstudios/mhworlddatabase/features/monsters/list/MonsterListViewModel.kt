package com.gatheringhallstudios.mhworlddatabase.features.monsters.list

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.types.MonsterSize
import com.gatheringhallstudios.mhworlddatabase.data.models.MonsterBase

/**
 * A viewmodel for any monster list fragment
 * Created by Carlos on 3/4/2018.
 */
class MonsterListViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).monsterDao()

    private var currentTab: MonsterSize? = null
    lateinit var monsters: LiveData<List<MonsterBase>>

    /**
     * Sets this viewmodel's "selected tab". Null means load all monsters
     */
    fun setTab(size: MonsterSize?) {
        if (::monsters.isInitialized && currentTab == size) {
            return
        }

        currentTab = size
        monsters = dao.loadMonsters(AppSettings.dataLocale, size)
    }
}
