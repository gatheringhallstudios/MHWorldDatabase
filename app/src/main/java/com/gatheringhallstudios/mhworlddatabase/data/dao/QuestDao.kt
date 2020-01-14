package com.gatheringhallstudios.mhworlddatabase.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.models.QuestBase
import com.gatheringhallstudios.mhworlddatabase.data.models.QuestMonster
import com.gatheringhallstudios.mhworlddatabase.data.models.QuestReward
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
    abstract fun loadQuests(langId: String, categories: Array<QuestCategory>): LiveData<List<QuestBase>>

    @Query("""
        SELECT q.id, category, stars, name, quest_type, objective, description, location_id, zenny
        FROM quest q
            JOIN quest_text qt
                ON qt.id = q.id
        WHERE lang_id = :langId
          AND q.id = :questId
    """)
    abstract fun loadQuest(langId: String, questId: Int): LiveData<QuestBase>

    @Query("""
        SELECT r.quest_id, r.`group`, r.item_id, r.stack, r.percentage,
            i.id item_id, it.name item_name, i.icon_name item_icon_name, i.category item_category,
            i.icon_color item_icon_color
        FROM quest_reward r
            JOIN item i
                ON i.id = r.item_id
            JOIN item_text it
                ON it.id = i.id
                AND it.lang_id = :langId
        WHERE r.quest_id = :questId
        ORDER BY r.`group` ASC, r.percentage DESC
    """)
    abstract fun loadQuestRewards(langId: String, questId: Int): LiveData<List<QuestReward>>

    @Query("""
        SELECT qm.monster_id, mtext.name monster_name, m.size monster_size,
            qm.quantity, qm.is_objective
        FROM quest_monster qm
            JOIN monster m
                ON m.id = qm.monster_id
            JOIN monster_text mtext
                ON mtext.id = m.id
                AND mtext.lang_id = :langId
        WHERE qm.quest_id = :questId
        ORDER BY is_objective DESC, m.order_id ASC
    """)
    abstract fun loadQuestMonsters(langId: String, questId: Int): LiveData<List<QuestMonster>>
}