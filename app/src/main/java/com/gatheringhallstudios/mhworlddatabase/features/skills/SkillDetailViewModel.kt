package com.gatheringhallstudios.mhworlddatabase.features.skills

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.ArmorDao
import com.gatheringhallstudios.mhworlddatabase.data.dao.CharmDao
import com.gatheringhallstudios.mhworlddatabase.data.dao.SkillDao
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorSkillView
import com.gatheringhallstudios.mhworlddatabase.data.views.CharmSkillView
import com.gatheringhallstudios.mhworlddatabase.data.views.SkillTreeFull

class SkillDetailViewModel(application: Application) :  AndroidViewModel(application) {
    private val skillDao : SkillDao = MHWDatabase.getDatabase(application).skillDao()

    private var id: Int = 0
    lateinit var skillTreeFull: LiveData<SkillTreeFull>
    lateinit var armorPieces: LiveData<List<ArmorSkillView>>
    lateinit var charms: LiveData<List<CharmSkillView>>

    fun setSkill(skillTreeId : Int) {
        if(this.id == skillTreeId) {
            return
        }

        this.id = skillTreeId
        skillTreeFull = skillDao.loadSkillTree(AppSettings.dataLocale, skillTreeId)
        armorPieces = skillDao.loadArmorWithSkill(AppSettings.dataLocale, skillTreeId)
        charms = skillDao.loadCharmsWithSkill(AppSettings.dataLocale, skillTreeId)
    }
}