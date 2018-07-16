package com.gatheringhallstudios.mhworlddatabase.features.decorations

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.DecorationDao
import com.gatheringhallstudios.mhworlddatabase.data.dao.SkillDao
import com.gatheringhallstudios.mhworlddatabase.data.views.DecorationView
import com.gatheringhallstudios.mhworlddatabase.data.views.SkillTreeFull

class DecorationDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val decorationDao: DecorationDao = MHWDatabase.getDatabase(application).decorationDao()
    private val skillDao: SkillDao = MHWDatabase.getDatabase(application).skillDao()

    private var id: Int = -1
    private var skillTreeId: Int = -1
    lateinit var decorationData: LiveData<DecorationView>
    lateinit var skillTreeData : LiveData<SkillTreeFull>

    fun setDecoration(decorationId: Int) {
        if (this.id == decorationId) {
            return
        }

        this.id = decorationId
        decorationData = decorationDao.loadDecoration(AppSettings.dataLocale, decorationId)
        skillTreeData = Transformations.switchMap(decorationData, ::setSkill)
    }

    private fun setSkill(decorationFullView: DecorationView): LiveData<SkillTreeFull>? {
        if (this.skillTreeId == decorationFullView.data.skilltree_id) {
            return null
        }

        this.skillTreeId = decorationFullView.data.skilltree_id
        return skillDao.loadSkillTree(AppSettings.dataLocale, skillTreeId)
    }
}