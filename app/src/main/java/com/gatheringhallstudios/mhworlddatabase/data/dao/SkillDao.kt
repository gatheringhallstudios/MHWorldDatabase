package com.gatheringhallstudios.mhworlddatabase.data.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Dao
import androidx.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.models.*

/**
 * A class used to retrieve data about skills centered from the skills point of view.
 * For skills about a piece of Armor, look at the ArmorDao instead.
 * Retrieve a copy of this class from MHWDatabase.skillDao().
 * Created by Carlos on 3/21/2018.
 */
@Dao
abstract class SkillDao {
    @Query("""
        SELECT id, name, max_level, description, icon_color, secret, unlocks_id
        FROM skilltree s join skilltree_text st USING (id)
        WHERE lang_id = :langId
        ORDER BY name """)
    abstract fun loadSkillTrees(langId: String): LiveData<List<SkillTree>>

    /**
     * Performs a full load on a skill tree.
     */
    fun loadSkillTree(langId: String, skillTreeId: Int): LiveData<SkillTreeFull> {
        val skillFull = loadSkills(langId, skillTreeId)

        return Transformations.map(skillFull) { data ->
            val firstItem = data.first()
            val skills = data.map {
                Skill(skilltree_id = it.id, level = it.level, description = it.description)
            }

            SkillTreeFull(
                    id = firstItem.id,
                    name = firstItem.skilltree_name,
                    max_level = firstItem.max_level,
                    secret = firstItem.secret,
                    description = firstItem.skilltree_description,
                    icon_color = firstItem.icon_color,
                    unlocks_id = firstItem.unlocks_id,
                    skills = skills
            )
        }
    }


    // internal class used by the loadSkillTree function
    protected data class SkillWithSkillTree(
            val id: Int,
            val skilltree_name: String?,
            val skilltree_description: String?,
            val max_level: Int,
            val level: Int,
            val secret: Int,
            val unlocks_id: Int?,
            val description: String?,
            val icon_color: String?
    )

    // internal query used by "loadSkillTree"
    @Query("""
        SELECT st.id, stt.name skilltree_name, st.secret, st.unlocks_id, stt.description skilltree_description,
            st.max_level, s.level, s.description, st.icon_color
        FROM skilltree st
            JOIN skilltree_text stt
                ON stt.id = st.id
                AND stt.lang_id = :langId
            JOIN skill s
                ON s.skilltree_id = st.id
                AND s.lang_id = :langId
        WHERE st.id = :skillTreeId """)
    protected abstract fun loadSkills(langId: String, skillTreeId: Int): LiveData<List<SkillWithSkillTree>>


    @Query("""
        SELECT c.id AS charm_id, c.previous_id AS charm_previous_id, c.rarity AS charm_rarity,
             ct.name AS charm_name, st.id skill_id, stext.name skill_name, st.max_level skill_max_level, st.secret skill_secret, st.unlocks_id skill_unlocks_id,
             st.icon_color skill_icon_color, cs.level level
        FROM charm c
            JOIN charm_text ct USING (id)
            JOIN charm_skill cs ON (c.id = charm_id)
            JOIN skilltree st ON (st.id = skilltree_id)
            JOIN skilltree_text stext
              ON stext.id = st.id
              AND stext.lang_id = ct.lang_id
        WHERE ct.lang_id = :langId
            AND cs.skilltree_id = :skillTreeId
    """)
    abstract fun loadCharmsWithSkill(langId: String, skillTreeId: Int): LiveData<List<CharmSkillLevel>>


    @Query("""
        SELECT stt.id skill_id, stext.name skill_name, stt.max_level skill_max_level, stt.secret skill_secret, stt.unlocks_id skill_unlocks_id, stt.icon_color skill_icon_color,
            a.id armor_id, at.name armor_name, a.rarity armor_rarity, a.rank armor_rank, a.armor_type armor_armor_type,
            askill.level level, a.slot_1 armor_slot_1, a.slot_2 armor_slot_2, a.slot_3 armor_slot_3
        FROM armor a
            JOIN armor_text at ON a.id = at.id
            JOIN armor_skill askill ON a.id = askill.armor_id
            JOIN skilltree stt ON askill.skilltree_id = stt.id
            JOIN skilltree_text stext
                ON stext.id = stt.id
                AND stext.lang_id = at.lang_id
        WHERE at.lang_id = :langId
          AND askill.skilltree_id = :skillTreeId
        ORDER BY a.id ASC""")
    abstract fun loadArmorWithSkill(langId: String, skillTreeId: Int): LiveData<List<ArmorSkillLevel>>

    @Query("""
        SELECT
            stt.id skill_id, stext.name skill_name, stt.max_level skill_max_level, stt.secret skill_secret, stt.unlocks_id skill_unlocks_id, stt.icon_color skill_icon_color,
            d.id deco_id, dt.name deco_name, d.slot deco_slot, d.icon_color deco_icon_color, d.skilltree_level level            
        FROM decoration d
            JOIN decoration_text dt USING (id)
            JOIN skilltree stt ON (stt.id = d.skilltree_id)
            JOIN skilltree_text stext
                ON stext.id = stt.id
                AND stext.lang_id = dt.lang_id
        WHERE d.skilltree_id = :skillTreeId
          AND dt.lang_id = :langId
        ORDER BY dt.name ASC""")
    abstract fun loadDecorationsWithSkill(langId: String, skillTreeId: Int): LiveData<List<DecorationSkillLevel>>

    @Query("""
        SELECT stt.id skilltree_id, stext.name skilltree_name, stt.max_level skilltree_max_level, stt.secret skilltree_secret, stt.unlocks_id skill_unlocks_id, stt.icon_color skilltree_icon_color,
            abs.setbonus_id as id, abt.name, abs.required
        FROM armorset_bonus_skill abs
            JOIN armorset_bonus_text abt ON (abt.id = abs.setbonus_id)
            JOIN skilltree stt ON (stt.id = abs.skilltree_id)
            JOIN skilltree_text stext
                ON stext.id = stt.id
                AND stext.lang_id = abt.lang_id
        WHERE abs.skilltree_id = :skillTreeId
          AND abt.lang_id = :langId
        ORDER BY abt.name ASC""")
    abstract fun loadSetBonusesWithSkill(langId: String, skillTreeId: Int): LiveData<List<ArmorSetBonus>>
}
