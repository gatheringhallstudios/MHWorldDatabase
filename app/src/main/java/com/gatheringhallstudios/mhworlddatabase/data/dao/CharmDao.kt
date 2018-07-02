package com.gatheringhallstudios.mhworlddatabase.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.views.CharmSkillView

@Dao
abstract class CharmDao {
    @Query("""
        SELECT c.*, ct.name, cs.level skillLevel
            FROM charm c
             JOIN charm_text ct USING (id)
             JOIN charm_skill cs ON (id = charm_id)
         WHERE ct.lang_id = :langId
            AND cs.skilltree_id = :skillTreeId

    """)
abstract fun loadCharms(langId: String, skillTreeId: Int) : LiveData<List<CharmSkillView>>

}