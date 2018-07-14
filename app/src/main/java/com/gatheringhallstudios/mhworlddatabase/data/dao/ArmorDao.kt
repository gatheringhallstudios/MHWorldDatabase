package com.gatheringhallstudios.mhworlddatabase.data.dao

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.entities.ArmorEntity
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.views.*

/**
 * Created by Carlos on 3/21/2018.
 */
@Dao
abstract class ArmorDao {
    @Query("""
        SELECT a.*, at.name, ast.name armorset_name
        FROM armor a
            JOIN armor_text at USING (id)
            JOIN armorset_text ast
                ON ast.id = a.armorset_id
                AND ast.lang_id = at.lang_id
        WHERE at.lang_id = :langId
          AND (:rank IS NULL OR a.rank = :rank)
    """)
    abstract fun loadArmorList(langId: String, rank: Rank?): LiveData<List<ArmorView>>

    @Query("""
        SELECT a.*, at.name, ast.name armorset_name
        FROM armor a
            JOIN armor_text at USING (id)
            JOIN armorset_text ast
                ON ast.id = a.armorset_id
                AND ast.lang_id = at.lang_id
        WHERE at.lang_id = :langId
        AND a.id = :armorId""")
    abstract fun loadArmor(langId: String, armorId: Int): LiveData<ArmorView>

    /**
     * Generates a list of ArmorSets with embedded ArmorViews
     * Equivalent to loadArmorList and then grouping
     */
    fun loadArmorSets(langId: String, rank: Rank): LiveData<List<ArmorSetView>> {
        // Load raw view of Armor with Armor Set info
        val armorSets = loadArmorList(langId, rank)

        return Transformations.map(armorSets) { data ->
            // Create a map of armorset_id -> ArmorView
            val setToArmorMap = data.groupBy { it.armorset_id }

            val armorSetList = mutableListOf<ArmorSetView>()

            for (armorGroup in setToArmorMap) {
                val armorSetId = armorGroup.value.first().armorset_id
                val armorSetName = armorGroup.value.first().armorset_name
                val armorList = armorGroup.value

                armorSetList.add(ArmorSetView(armorSetId, armorSetName, armorList))
            }

            armorSetList
        }
    }

    @Query("""
        SELECT abs.*, abt.name, st.*, stt.name AS skillName
        FROM armorset_bonus_skill abs
            JOIN armorset_bonus_text abt
                ON abt.id = abs.setbonus_id
            JOIN skilltree st
                ON abs.skilltree_id = st.id
            JOIN skilltree_text stt
                ON abs.skilltree_id = stt.id
                AND abt.lang_id = stt.lang_id
        WHERE abt.lang_id = :langId
        AND abs.setbonus_id= :setBonusId""")
    abstract fun loadArmorSetBonus(langId: String, setBonusId: Int) : LiveData<List<ArmorSetBonusView>>

    @Query("""
        SELECT a.*, it.name
        FROM armor_recipe a
            JOIN item_text it
                ON a.item_id = it.name
        WHERE it.lang_id = :langId
        AND a.armor_id= :armorId""")
    abstract fun loadArmorComponentViews(langId: String, armorId: Int) : LiveData<List<ArmorComponentView>>

    @Query("""
        SELECT a.*, askill.level skillLevel
            FROM armor a
            JOIN armor_skill askill ON (armor_id = id)
            JOIN skilltree_text
            WHERE at.lang_id = :langId
               AND askill.skilltree_id = :skillTreeId
            ORDER BY a.id ASC""")
    abstract fun loadArmorSkills(langId: String, setBonusId: Int) : LiveData<List<ArmorSkillView>>
}
