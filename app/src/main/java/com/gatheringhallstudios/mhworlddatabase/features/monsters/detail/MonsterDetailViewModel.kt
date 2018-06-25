package com.gatheringhallstudios.mhworlddatabase.features.monsters.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings

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

        val lang = AppSettings.dataLocale
        monster = dao.loadMonster(lang, monsterId)
        habitats = dao.loadHabitats(lang, monsterId)
        rewards = dao.loadRewards(lang, monsterId)
        hitzones = dao.loadHitzones(lang, monsterId)
        breaks = dao.loadBreaks(lang, monsterId)
    }
}
