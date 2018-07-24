package com.gatheringhallstudios.mhworlddatabase.data.dao

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.models.*

@Dao
abstract class CharmDao {
    @Query("""
        SELECT c.*, ct.name
        FROM charm c
            JOIN charm_text ct
                ON ct.id = c.id
                AND ct.lang_id = :langId
        ORDER BY ct.name""")
    abstract fun loadCharms(langId: String): LiveData<List<CharmBase>>

    @Query("""
        SELECT c.*, ct.name, cr.quantity AS component_quantity, cs.level AS skillLevel, cs.skilltree_id, i.id AS component_id, it.name AS component_name, s.icon_color AS skillIconColor, st.name AS skillName
        FROM charm c
            JOIN charm_text ct
                ON ct.id = c.id
                AND ct.lang_id = :langId
            JOIN charm_recipe cr
                ON c.id = cr.charm_id
            JOIN charm_skill cs
                ON c.id = cs.charm_id
            JOIN item i
                ON cr.item_id = i.id
            JOIN item_text it
                ON cr.item_id = it.id
                AND  it.lang_id = :langId
            JOIN skilltree s
                ON cs.skilltree_id = s.id
            JOIN skilltree_text st
                ON cs.skilltree_id = st.id
                AND st.lang_id = :langId
        WHERE c.id = :charmId""")
    abstract fun loadCharm(langId: String, charmId: Int): LiveData<List<Charm>>

    fun loadCharmFull(langId: String, charmId: Int): LiveData<CharmFull> {
        val charmFull = loadCharm(langId, charmId)

        return Transformations.map(charmFull) { data ->
            val firstItem = data.first()
            val components = data.map {
                CharmComponent(result = it.component.result, quantity = it.component.quantity)
            }

            CharmFull(
                    components = components,
                    data = firstItem.data,
                    name = firstItem.name,
                    description = firstItem.description,
                    skillLevel = firstItem.skillLevel,
                    skilltree_id = firstItem.skilltree_id,
                    skillName = firstItem.skillName,
                    skillIconColor = firstItem.skillIconColor
            )
        }
    }
}