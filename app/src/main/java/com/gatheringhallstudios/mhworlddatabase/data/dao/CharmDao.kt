package com.gatheringhallstudios.mhworlddatabase.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.models.Charm
import com.gatheringhallstudios.mhworlddatabase.data.models.CharmFull
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemQuantity
import com.gatheringhallstudios.mhworlddatabase.data.models.SkillLevel
import com.gatheringhallstudios.mhworlddatabase.util.createLiveData

@Dao
abstract class CharmDao {
    /**
     * Synchronously loads charms
     */
    @Query("""
        SELECT c.*, ct.name
        FROM charm c
            JOIN charm_text ct
                ON ct.id = c.id
                AND ct.lang_id = :langId
        ORDER BY ct.name""")
    abstract fun loadCharmsSync(langId: String): List<Charm>

    @Query("""
        SELECT c.*, ct.name
        FROM charm c
            JOIN charm_text ct
                ON ct.id = c.id
                AND ct.lang_id = :langId
        ORDER BY ct.name""")
    abstract fun loadCharmList(langId: String): LiveData<List<Charm>>

    @Query("""
        SELECT c.*, ct.name
        FROM charm c
            JOIN charm_text ct
                ON ct.id = c.id
                AND ct.lang_id = :langId
        ORDER BY ct.name""")
    abstract fun loadCharmListSync(langId: String): List<Charm>

    /**
     * Loads full data for a charm asynchronously.
     * Full data includes all join tables like items and skills
     */
    fun loadCharmFull(langId: String, charmId: Int) = createLiveData {
        CharmFull(
                charm = loadCharmSync(langId, charmId),
                skills = loadCharmSkillsSync(langId, charmId),
                components = loadCharmComponentsSync(langId, charmId)
        )
    }

    fun loadCharmFullSync(langId: String, charmId: Int): CharmFull {
        return CharmFull(
                charm = loadCharmSync(langId, charmId),
                skills = loadCharmSkillsSync(langId, charmId),
                components = loadCharmComponentsSync(langId, charmId)
        )
    }

    fun loadCharmAndSkillList(langId: String): List<CharmFull> {
        val charms = loadCharmListSync(langId)

        return charms.map {
            CharmFull(
                    charm = it,
                    skills = loadCharmSkillsSync(langId, it.id),
                    components = emptyList()
            )
        }
    }

    @Query("""
        SELECT c.*, ct.name
        FROM charm c JOIN charm_text ct USING (id)
        WHERE ct.lang_id = :langId
          AND c.id = :charmId
    """)
    protected abstract fun loadCharmSync(langId: String, charmId: Int): Charm

    @Query("""
        SELECT i.id item_id, it.name item_name, i.icon_name item_icon_name,
            i.category item_category, i.icon_color item_icon_color, cr.quantity
        FROM charm c
            JOIN recipe_item cr
                ON cr.recipe_id = c.recipe_id
            JOIN item i
                ON i.id = cr.item_id
            JOIN item_text it
                ON it.id = i.id
                AND it.lang_id = :langId
        WHERE c.id = :charmId
    """)
    protected abstract fun loadCharmComponentsSync(langId: String, charmId: Int): List<ItemQuantity>

    @Query("""
        SELECT s.id skill_id, stt.name skill_name, s.max_level skill_max_level,
            s.icon_color skill_icon_color, cs.level level
        FROM charm_skill cs
            JOIN skilltree s
                ON cs.skilltree_id = s.id
            JOIN skilltree_text stt
                ON stt.id = s.id
        WHERE stt.lang_id = :langId
          AND cs.charm_id = :charmId
    """)
    protected abstract fun loadCharmSkillsSync(langId: String, charmId: Int): List<SkillLevel>
}