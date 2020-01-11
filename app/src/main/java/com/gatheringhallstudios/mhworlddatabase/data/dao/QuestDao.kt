package com.gatheringhallstudios.mhworlddatabase.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.models.QuestBase
import com.gatheringhallstudios.mhworlddatabase.data.types.QuestCategory

@Dao
abstract class QuestDao {
    @Query("""
        SELECT q.id, category, stars, name, quest_type, objective, description, location_id, zenny
        FROM quest q
            JOIN quest_text qt
                ON qt.id = q.id
        WHERE lang_id = :langId
          AND q.category in (:categories)
        ORDER BY q.order_id ASC
    """)
    abstract fun getQuests(langId: String, categories: Array<QuestCategory>): LiveData<List<QuestBase>>

    @Query("""
        SELECT q.id, category, stars, name, quest_type, objective, description, location_id, zenny
        FROM quest q
            JOIN quest_text qt
                ON qt.id = q.id
        WHERE lang_id = :langId
          AND q.id = :questId
    """)
    abstract fun getQuest(langId: String, questId: Int): LiveData<QuestBase>

}