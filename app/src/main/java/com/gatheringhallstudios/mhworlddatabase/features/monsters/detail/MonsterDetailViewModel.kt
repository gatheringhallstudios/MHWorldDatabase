package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings

import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.*

/**
 * A ViewModel for any monster summary fragment
 */
class MonsterDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val db = MHWDatabase.getDatabase(application)
    private val dao = db.monsterDao()

    private var id: Int = -1

    lateinit var monster: LiveData<Monster>
    lateinit var habitats: LiveData<List<MonsterHabitat>>
    lateinit var rewards: LiveData<List<MonsterReward>>
    lateinit var hitzones: LiveData<List<MonsterHitzone>>
    lateinit var breaks: LiveData<List<MonsterBreak>>

    fun setMonster(monsterId: Int) {
        if (id == monsterId) {
            return
        }

        // Query monster by ID
        this.id = monsterId

        val lang = AppSettings.dataLocale
        monster = dao.loadMonster(lang, monsterId)
        habitats = dao.loadHabitats(lang, monsterId)
        rewards = dao.loadRewards(lang, monsterId)
        hitzones = dao.loadHitzones(lang, monsterId)
        breaks = dao.loadBreaks(lang, monsterId)
    }
}
