package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.MonsterDao
import com.gatheringhallstudios.mhworlddatabase.data.views.*

/**
 * A ViewModel for any monster summary fragment
 */
class MonsterDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val db = MHWDatabase.getDatabase(application)
    private val dao = db.monsterDao()

    private var id: Int = -1

    lateinit var monster: LiveData<MonsterView>
    lateinit var habitats: LiveData<List<MonsterHabitatView>>
    lateinit var rewards: LiveData<List<MonsterRewardView>>
    lateinit var hitzones: LiveData<List<MonsterHitzoneView>>
    lateinit var breaks: LiveData<List<MonsterBreakView>>

    fun setMonster(monsterId: Int) {
        if (id == monsterId) {
            return
        }

        // Query monster by ID
        this.id = monsterId
        monster = dao.loadMonster("en", monsterId)
        habitats = dao.loadHabitats("en", monsterId)
        rewards = dao.loadRewards("en", monsterId)
        hitzones = dao.loadHitzones("en", monsterId)
        breaks = dao.loadBreaks("en", monsterId)
    }
}
