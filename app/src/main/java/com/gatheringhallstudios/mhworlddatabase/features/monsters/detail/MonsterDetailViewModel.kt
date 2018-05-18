package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.MonsterDao
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterHabitatView
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterHitzoneView
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterRewardView
import com.gatheringhallstudios.mhworlddatabase.data.views.MonsterView

/**
 * A ViewModel for any monster summary fragment
 */
class MonsterDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: MonsterDao

    init {
        // todo: perhaps inject the database directly?
        val db = MHWDatabase.getDatabase(application)
        dao = db.monsterDao()
    }

    private var id: Int = 0

    private var initialized = false
    lateinit var monster: LiveData<MonsterView>
    lateinit var habitats: LiveData<List<MonsterHabitatView>>
    lateinit var rewards: LiveData<List<MonsterRewardView>>
    lateinit var hitzones: LiveData<List<MonsterHitzoneView>>

    fun setMonster(monsterId: Int) {
        if (initialized) {
            return
        }

        // Query monster by ID
        this.id = monsterId
        monster = dao.loadMonster("en", monsterId)
        habitats = dao.loadHabitats("en", monsterId)
        rewards = dao.loadRewards("en", monsterId)
        hitzones = dao.loadHitzones("en", monsterId)

        initialized = true
    }
}
