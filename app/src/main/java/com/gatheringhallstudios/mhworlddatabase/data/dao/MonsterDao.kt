package com.gatheringhallstudios.mhworlddatabase.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

import com.gatheringhallstudios.mhworlddatabase.data.types.MonsterSize
import com.gatheringhallstudios.mhworlddatabase.data.views.*

/**
 * Created by Carlos on 3/21/2018.
 */
@Dao
abstract class MonsterDao {
    @Query("""
        SELECT m.*, t.name, t.description
        from monster m JOIN monster_text t USING (id)
        WHERE t.lang_id = :langId
        ORDER BY t.name ASC""")
    abstract fun loadList(langId: String): LiveData<List<MonsterView>>

    @Query("""
        SELECT m.*, t.name, t.description
        from monster m JOIN monster_text t USING (id)
        WHERE t.lang_id = :langId
          AND m.size = :size
        ORDER BY t.name ASC""")
    abstract fun loadList(langId: String, size: MonsterSize): LiveData<List<MonsterView>>

    @Query("""
        SELECT m.*, t.name, t.description
        from monster m JOIN monster_text t USING (id)
        WHERE t.lang_id = :langId AND m.id = :id
        LIMIT 1""")
    abstract fun loadMonster(langId: String, id: Int): LiveData<MonsterView>

    @Query("""
        SELECT h.*, lt.name location_name
        FROM monster_habitat h
            JOIN location_text lt
            ON lt.id = h.location_id
            AND lt.lang_id = :langId
        WHERE h.monster_id = :monsterId
        ORDER BY h.id""")
    abstract fun loadHabitats(langId: String, monsterId: Int): LiveData<List<MonsterHabitatView>>

    @Query("""
        SELECT h.*, pt.name body_part
        FROM monster_hitzone h
            JOIN monster_hitzone_text pt
                ON pt.id = h.id
                AND pt.lang_id = :langId
        WHERE h.monster_id = :monsterId
        ORDER BY h.id""")
    abstract fun loadHitzones(langId: String, monsterId: Int): LiveData<List<MonsterHitzoneView>>

    @Query("""
        SELECT b.*, bt.part_name
        FROM monster_break b JOIN monster_break_text bt
            ON bt.id = b.id
        WHERE b.monster_id = :monsterId
            AND bt.lang_id = :langId
        ORDER BY b.id""")
    abstract fun loadBreaks(langId: String, monsterId: Int): LiveData<List<MonsterBreakView>>

    @Query("""
        SELECT r.*, ct.name condition_name, it.name item_name
        FROM monster_reward r
            JOIN monster_reward_condition_text ct
                ON ct.id = r.condition_id
                AND ct.lang_id = :langId
            JOIN item_text it
                ON it.id = r.item_id
                AND it.lang_id = :langId
        WHERE r.monster_id = :monsterId ORDER BY r.id""")
    abstract fun loadRewards(langId: String, monsterId: Int): LiveData<List<MonsterRewardView>>
}
