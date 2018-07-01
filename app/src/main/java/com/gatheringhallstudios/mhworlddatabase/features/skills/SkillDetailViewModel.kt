package com.gatheringhallstudios.mhworlddatabase.features.skills

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.ArmorDao
import com.gatheringhallstudios.mhworlddatabase.data.dao.SkillDao
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorBasicView
import com.gatheringhallstudios.mhworlddatabase.data.views.SkillTreeFull

class SkillDetailViewModel(application: Application) :  AndroidViewModel(application) {
    private val skillDao : SkillDao = MHWDatabase.getDatabase(application).skillDao()
    private val armorDao : ArmorDao = MHWDatabase.getDatabase(application).armorDao()


    private var id: Int = 0
    lateinit var skillTreeFull: LiveData<SkillTreeFull>
    lateinit var armorPieces: LiveData<List<ArmorBasicView>>

    fun setSkill(skillTreeId : Int) {
        if(this.id == skillTreeId) {
            return
        }

        this.id = skillTreeId
        skillTreeFull = skillDao.loadSkillTree(AppSettings.dataLocale, skillTreeId)
        armorPieces = armorDao.loadArmorWithSkil(AppSettings.dataLocale, skillTreeId)
    }
}