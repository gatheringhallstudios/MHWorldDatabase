package com.gatheringhallstudios.mhworlddatabase.data.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Dao
import androidx.room.Query
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.Rank
import com.gatheringhallstudios.mhworlddatabase.data.views.Armor
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorBasicView
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorSetView

/**
 * Created by Carlos on 3/21/2018.
 */
@Dao
abstract class ArmorDao {
    @Query("""
        SELECT a.*, at.name
            FROM armor a JOIN armor_text at USING (id)
            WHERE at.lang_id = :langId
               AND a.rarity BETWEEN :minRarity AND :maxRarity
            ORDER BY a.id ASC""")
    abstract fun loadArmorList(langId: String, minRarity: Int, maxRarity: Int): LiveData<List<ArmorBasicView>>

    fun loadArmorList(langId: String): LiveData<List<ArmorBasicView>> {
        return loadArmorList(langId, 0, 999)
    }

    @Query("""
        SELECT a.*, at.name
            FROM armor a JOIN armor_text at USING (id)
            WHERE at.lang_id = :langId
            AND a.id = :armorId""")
    abstract fun loadArmor(langId: String, armorId: Int): LiveData<Armor>

    /**
     * Generates a list of ArmorSets with embedded ArmorBasicViews
     */
    fun loadArmorSets(langId: String, rank: Rank): LiveData<List<ArmorSetView>> {
        // Load raw view of Armor with Armor Set info
        val armorSets = loadArmorWithArmorSets(langId, rank)

        return Transformations.map(armorSets) { data ->
            // Create a map of armorset_id -> ArmorBasicView
            val setToArmorMap = data.groupBy { it.armorset_id }

            val armorSetList = mutableListOf<ArmorSetView>()

            for (armor in setToArmorMap) {
                val armorSetId = armor.value.first().armorset_id
                val armorSetName = armor.value.first().armorset_name
                val typeToArmorMap = armor.value.associateBy(
                        { it.armor_type },
                        { ArmorBasicView(it.armor_id, it.armor_name, it.rarity, it.rank, it.armor_type, it.armorset_id) })

                armorSetList.add(
                        ArmorSetView(
                                armorSetId,
                                armorSetName,
                                typeToArmorMap[ArmorType.HEAD],
                                typeToArmorMap[ArmorType.CHEST],
                                typeToArmorMap[ArmorType.ARMS],
                                typeToArmorMap[ArmorType.WAIST],
                                typeToArmorMap[ArmorType.LEGS])
                )
            }

            armorSetList
        }
    }

    /**
     * Loads a denormalized list of Armors with ArmorSet data attached
     */
    @Query("""
        SELECT ast.id armorset_id, ast.name armorset_name,
                a.id armor_id, at.name armor_name, a.rarity, a.rank, a.armor_type
         FROM armor a
         JOIN armor_text at USING (id)
         JOIN armorset_text ast ON ast.id = a.armorset_id
         WHERE at.lang_id = :langId AND a.rank = :rank
         ORDER BY a.armorset_id ASC, a.id ASC
    """)
    abstract fun loadArmorWithArmorSets(langId: String, rank: Rank): LiveData<List<ArmorSetWithArmor>>

    // Denormalized class used by loadArmorSets
    data class ArmorSetWithArmor(
            val armorset_id: Int,
            val armorset_name: String?,
            val armor_id: Int,
            val armor_name: String?,
            val rarity: Int,
            val rank: Rank,
            val armor_type: ArmorType
    )
}
