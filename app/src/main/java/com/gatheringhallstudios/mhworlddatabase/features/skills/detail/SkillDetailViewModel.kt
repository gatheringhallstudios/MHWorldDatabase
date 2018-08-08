package com.gatheringhallstudios.mhworlddatabase.features.skills.detail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.SkillDao
import com.gatheringhallstudios.mhworlddatabase.data.models.*

class SkillDetailViewModel(application: Application) :  AndroidViewModel(application) {
    private val skillDao : SkillDao = MHWDatabase.getDatabase(application).skillDao()

    private var id: Int = 0
    lateinit var skillTreeFull: LiveData<SkillTreeFull>
    lateinit var armorPieces: LiveData<List<ArmorSkillLevel>>
    lateinit var charms: LiveData<List<CharmSkillLevel>>
    lateinit var decorations: LiveData<List<DecorationSkillLevel>>
    lateinit var bonuses: LiveData<List<ArmorSetBonus>>

    fun setSkill(skillTreeId : Int) {
        if (this.id == skillTreeId) {
            return
        }

        this.id = skillTreeId

        val lang = AppSettings.dataLocale
        skillTreeFull = skillDao.loadSkillTree(lang, skillTreeId)
        armorPieces = skillDao.loadArmorWithSkill(lang, skillTreeId)
        charms = skillDao.loadCharmsWithSkill(lang, skillTreeId)
        decorations = skillDao.loadDecorationsWithSkill(lang, skillTreeId)
        bonuses = skillDao.loadSetBonusesWithSkill(lang, skillTreeId)
    }
}