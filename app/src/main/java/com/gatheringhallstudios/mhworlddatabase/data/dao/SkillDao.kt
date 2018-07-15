package com.gatheringhallstudios.mhworlddatabase.data.dao

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.views.*

/**
 * A class used to retrieve data about skills centered from the skills point of view.
 * For skills about a piece of Armor, look at the ArmorDao instead.
 * Retrieve a copy of this class from MHWDatabase.skillDao().
 * Created by Carlos on 3/21/2018.
 */
@Dao
abstract class SkillDao {
    @Query("""
        SELECT id, name, description, icon_color
        FROM skilltree s join skilltree_text st USING (id)
        WHERE lang_id = :langId
        ORDER BY name """)
    abstract fun loadSkillTrees(langId: String): LiveData<List<SkillTreeView>>

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
                    description = firstItem.skilltree_description,
                    icon_color = firstItem.icon_color,
                    skills = skills
            )
        }
    }


    // internal class used by the loadSkillTree function
    protected data class SkillWithSkillTree(
            val id: Int,
            val skilltree_name: String?,
            val skilltree_description: String?,
            val level: Int,
            val description: String?,
            val icon_color: String?
    )

    // internal query used by "loadSkillTree"
    @Query("""
        SELECT st.id, stt.name skilltree_name, stt.description skilltree_description,
            s.level, s.description, st.icon_color
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
        SELECT c.*, ct.name, cs.level skillLevel
            FROM charm c
             JOIN charm_text ct USING (id)
             JOIN charm_skill cs ON (id = charm_id)
         WHERE ct.lang_id = :langId
            AND cs.skilltree_id = :skillTreeId

    """)
    abstract fun loadCharmsWithSkill(langId: String, skillTreeId: Int) : LiveData<List<CharmSkillView>>


    @Query("""
        SELECT a.*, at.name, askill.skilltree_id, askill.level skillLevel, stt.icon_color
            FROM armor a
            JOIN armor_text at ON a.id = at.id
            JOIN armor_skill askill ON a.id = askill.armor_id
            JOIN skilltree stt ON askill.skilltree_id = stt.id
            WHERE at.lang_id = :langId
               AND askill.skilltree_id = :skillTreeId
            ORDER BY a.id ASC""")
    abstract fun loadArmorWithSkill(langId: String, skillTreeId: Int): LiveData<List<ArmorSkillView>>

    @Query("""
        SELECT d.id, dt.name
        FROM decoration d
            JOIN decoration_text dt
                ON dt.id = d.id
                AND dt.lang_id = :langId
        WHERE d.skilltree_id = :skillTreeId
        ORDER BY dt.name""")
    abstract fun loadDecorationsWithSkill(langId: String, skillTreeId: Int): LiveData<List<DecorationView>>
}
