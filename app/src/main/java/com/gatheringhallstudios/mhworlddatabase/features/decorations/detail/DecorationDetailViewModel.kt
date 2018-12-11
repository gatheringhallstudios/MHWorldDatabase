package com.gatheringhallstudios.mhworlddatabase.features.decorations.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.DecorationDao
import com.gatheringhallstudios.mhworlddatabase.data.dao.SkillDao
import com.gatheringhallstudios.mhworlddatabase.data.models.Decoration
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillTreeFull

class DecorationDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val decorationDao: DecorationDao = MHWDatabase.getDatabase(application).decorationDao()

    private var id: Int = -1
    lateinit var decorationData: LiveData<Decoration>

    fun setDecoration(decorationId: Int) {
        if (this.id == decorationId) {
            return
        }

        this.id = decorationId
        decorationData = decorationDao.loadDecoration(AppSettings.dataLocale, decorationId)
    }

}