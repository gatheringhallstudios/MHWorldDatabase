package com.gatheringhallstudios.mhworlddatabase.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query

import com.gatheringhallstudios.mhworlddatabase.data.types.MonsterSize
import com.gatheringhallstudios.mhworlddatabase.data.models.*

/**
 * Created by Carlos on 3/21/2018.
 */
@Dao
abstract class MonsterDao {
    @Query("""
        SELECT m.id, m.size, t.name
        from monster m JOIN monster_text t USING (id)
        WHERE t.lang_id = :langId
          AND (m.size = :size OR :size IS NULL)
        ORDER BY t.name ASC""")
    abstract fun loadMonsters(langId: String, size: MonsterSize? = null): LiveData<List<MonsterBase>>

    @Query("""
        SELECT m.*, t.name, t.ecology, t.description
        from monster m JOIN monster_text t USING (id)
        WHERE t.lang_id = :langId AND m.id = :id
        LIMIT 1""")
    abstract fun loadMonster(langId: String, id: Int): LiveData<Monster>

    @Query("""
        SELECT h.start_area, h.move_area, h.rest_area,
            lt.id location_id, lt.name location_name
        FROM monster_habitat h
            JOIN location_text lt
            ON lt.id = h.location_id
            AND lt.lang_id = :langId
        WHERE h.monster_id = :monsterId
        ORDER BY h.id""")
    abstract fun loadHabitats(langId: String, monsterId: Int): LiveData<List<MonsterHabitat>>

    @Query("""
        SELECT h.*, pt.name body_part
        FROM monster_hitzone h
            JOIN monster_hitzone_text pt
                ON pt.id = h.id
                AND pt.lang_id = :langId
        WHERE h.monster_id = :monsterId
        ORDER BY h.id""")
    abstract fun loadHitzones(langId: String, monsterId: Int): LiveData<List<MonsterHitzone>>

    @Query("""
        SELECT b.*, bt.part_name
        FROM monster_break b JOIN monster_break_text bt
            ON bt.id = b.id
        WHERE b.monster_id = :monsterId
            AND bt.lang_id = :langId
        ORDER BY b.id""")
    abstract fun loadBreaks(langId: String, monsterId: Int): LiveData<List<MonsterBreak>>

    @Query("""
        SELECT r.rank, ct.name condition_name, r.stack, r.percentage,
            i.id item_id, it.name item_name, i.icon_name item_icon_name, i.icon_color item_icon_color
        FROM monster_reward r
            JOIN monster_reward_condition_text ct
                ON ct.id = r.condition_id
                AND ct.lang_id = :langId
            JOIN item i
                ON i.id = r.item_id
            JOIN item_text it
                ON it.id = i.id
                AND it.lang_id = :langId
        WHERE r.monster_id = :monsterId ORDER BY r.id""")
    abstract fun loadRewards(langId: String, monsterId: Int): LiveData<List<MonsterReward>>
}
